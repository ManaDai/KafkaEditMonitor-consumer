

document.addEventListener('DOMContentLoaded', function () {
    const keywordLinks = document.querySelectorAll('.keyword-list a');
    const editLogElement = document.getElementById('edit-log');
    const maxEntries = 100; // 表示する最大エントリ数
    let currentKeywordId = null; // 現在のキーワードIDを追跡する変数
    const backButton = document.querySelector('#back-button');


    function createEventSource(keywordId) {
        // 古い EventSource インスタンスをクローズ
        if (window.eventSource) {
            window.eventSource.close();
            window.eventSource = null;
        }

        // 新しい EventSource インスタンスを作成
        const eventSource = new EventSource(`/api/recent-edits?keywordId=${keywordId}`);
        window.eventSource = eventSource;
        console.log('EventSource created:', eventSource);

        eventSource.addEventListener('edit', function (event) {
            console.log('Edit event received:', event);
            let edit;

            try {
                edit = JSON.parse(event.data);
                console.log('Parsed edit:', edit);
            } catch (e) {
                console.error('Failed to parse JSON:', e);
                return;
            }

            // サーバーからのエラーメッセージをチェック
            if (edit && edit.error) {
                console.error('Error received from server:', edit.error);
                return;
            }

            // 現在のキーワードが変更された場合のみ innerHTML をクリア
            if (editLogElement && keywordId === currentKeywordId) {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${edit.user || 'N/A'}</td>
                    <td><a href="${edit.titleUrl}" target="_blank">${edit.pageTitle || 'N/A'}</a></td>
                    <td>${edit.comment || 'N/A'}</td>
                    <td>${new Date(parseInt(edit.timestamp) * 1000).toLocaleString() || 'N/A'}</td>
                `;
                editLogElement.appendChild(row);

                // エントリが最大数を超えた場合、古いエントリを削除
                if (editLogElement.children.length > maxEntries) {
                    editLogElement.removeChild(editLogElement.firstChild);
                }
            }
        });

        eventSource.onerror = function (event) {
            console.error('EventSource failed:', event);
            // エラーメッセージの詳細を表示
            if (event.readyState === EventSource.CONNECTING) {
                console.log('EventSource is connecting.');
            } else if (event.readyState === EventSource.OPEN) {
                console.log('EventSource is open.');
            } else if (event.readyState === EventSource.CLOSED) {
                console.log('EventSource is closed.');
            }
            // Optionally, handle reconnection logic here
            if (event.target.readyState === EventSource.CLOSED) {
                console.log('Connection closed. Attempting to reconnect...');
                setTimeout(() => {
                    createEventSource(currentKeywordId);
                }, 5000); // 5秒後に再接続を試みる
            }
        };


    }

    keywordLinks.forEach(link => {
        link.addEventListener('click', function (event) {
            // event.preventDefault();
            if (this.classList.contains('keyword-btn')) {
                event.preventDefault();

                const keywordId = this.getAttribute('href').split('/').pop();

                // 新しいキーワードが選択された場合にのみ editLogElement をクリア
                if (keywordId !== currentKeywordId) {
                    if (editLogElement) {
                        editLogElement.innerHTML = ''; // 清空
                    }

                    currentKeywordId = keywordId; // 現在のキーワードIDを更新
                    createEventSource(keywordId);

                    document.getElementById('recent-edits').classList.remove('hidden');
                    document.getElementById('keyword-registration').classList.add('hidden');
                    // document.getElementById('stats').classList.remove('hidden');
                }
            }

            if (this.classList.contains('delete-btn')) {
                // event.preventDefault(); // リンクのデフォルト動作を防ぐ
                const id = this.getAttribute('data-id');

                fetch(`/keyword/delete-keyword/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (response.ok) {
                        window.location.href = '/dashboard';
                    } else {
                        alert('Error deleting keyword');
                    }
                });
            }
        });
    });

    window.addEventListener('beforeunload', function () {
        if (eventSource) {
            eventSource.close();
            console.log('EventSource manually closed on page unload.');
        }
    });


    backButton.addEventListener('click', function () {
        document.getElementById('recent-edits').classList.add('hidden');
        document.getElementById('keyword-registration').classList.remove('hidden');

    })


});

