(function () {
    // 1. Inject HTML for the chat widget
    const chatWidgetHTML = `
        <div id="ai-chat-widget-btn" class="ai-chat-widget-btn">
            <svg viewBox="0 0 24 24" width="30" height="30" fill="white">
                <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2z"/>
            </svg>
        </div>
        
        <div id="ai-chat-window" class="ai-chat-window">
            <div class="ai-chat-header">
                <span class="ai-chat-title">学科竞赛智能助手</span>
                <span id="ai-chat-close" class="ai-chat-close">&times;</span>
            </div>
            <div id="ai-chat-body" class="ai-chat-body">
                <div class="ai-chat-message ai">
                    你好！我是学科竞赛智能体助手，有什么可以帮你的吗？
                </div>
            </div>
            <div class="ai-chat-footer">
                <input type="text" id="ai-chat-input" class="ai-chat-input" placeholder="输入你的问题..." />
                <button id="ai-chat-send-btn" class="ai-chat-send-btn">发送</button>
            </div>
        </div>
    `;

    // Create a container and append to body
    const container = document.createElement('div');
    container.innerHTML = chatWidgetHTML;
    document.body.appendChild(container);

    // 2. Logic
    const widgetBtn = document.getElementById('ai-chat-widget-btn');
    const chatWindow = document.getElementById('ai-chat-window');
    const closeBtn = document.getElementById('ai-chat-close');
    const sendBtn = document.getElementById('ai-chat-send-btn');
    const chatInput = document.getElementById('ai-chat-input');
    const chatBody = document.getElementById('ai-chat-body');

    // Toggle window
    widgetBtn.addEventListener('click', () => {
        chatWindow.classList.toggle('active');
        if (chatWindow.classList.contains('active')) {
            setTimeout(() => chatInput.focus(), 300);
        }
    });

    closeBtn.addEventListener('click', () => {
        chatWindow.classList.remove('active');
    });

    // Send Message
    async function sendMessage() {
        const text = chatInput.value.trim();
        if (!text) return;

        // Add User Message
        appendMessage('user', text);
        chatInput.value = '';

        // Disable input while waiting
        disableInput(true);

        // Show Loading
        const loadingId = showLoading();

        try {
            // Call Backend API
            // Assuming the backend is running on the same domain usually at /springbootpx13e/ai/chat
            // Need to adjust the path based on actual deployment context path if needed
            // Based on index.html: context-path is /springbootpx13e ? No, usually simple relative path works if same origin.
            // Let's try relative path '../ai/chat' or '/springbootpx13e/ai/chat'
            // Use absolute path with context root
            const response = await fetch('/springbootpx13e/ai/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: text })
            });

            const data = await response.json();

            removeLoading(loadingId);

            if (data.code === 0) {
                appendMessage('ai', data.reply);
            } else {
                appendMessage('ai', "抱歉，出错了：" + (data.msg || "未知错误"));
            }

        } catch (error) {
            removeLoading(loadingId);
            appendMessage('ai', "网络连接失败，请稍后再试。");
            console.error("Chat Error:", error);
        } finally {
            disableInput(false);
            chatInput.focus();
        }
    }

    sendBtn.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    // Helper functions
    function appendMessage(role, text) {
        const div = document.createElement('div');
        div.className = `ai-chat-message ${role}`;
        div.innerText = text; // safety: innerText
        chatBody.appendChild(div);
        scrollToBottom();
    }

    function showLoading() {
        const id = 'loading-' + Date.now();
        const div = document.createElement('div');
        div.id = id;
        div.className = 'typing-indicator';
        div.innerHTML = `
            <div class="typing-dot"></div>
            <div class="typing-dot"></div>
            <div class="typing-dot"></div>
        `;
        chatBody.appendChild(div);
        scrollToBottom();
        return id;
    }

    function removeLoading(id) {
        const el = document.getElementById(id);
        if (el) el.remove();
    }

    function scrollToBottom() {
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function disableInput(disabled) {
        chatInput.disabled = disabled;
        sendBtn.disabled = disabled;
    }

})();
