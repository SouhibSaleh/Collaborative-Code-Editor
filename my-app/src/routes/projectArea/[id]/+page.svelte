<script lang="ts">
    import { onMount, onDestroy } from "svelte";
    import { page } from "$app/state";
    import { Client } from "@stomp/stompjs";
    import type {
        UpdateCharacter,
        Snapshot,
        Message,
        lockLineRequest,
        enterProjectRequest,
    } from "$lib/models";
    import SockJS from "sockjs-client";
    import type { AuthResponse } from "$lib/store/user";
    import { getDefaultSnapshot, saveSnapshot } from "$lib/api/snapshot";

    let client: Client;
    let textContent: string = $state(" ");
    let messageContent: string = $state("");
    let messages: Message[] = $state([]);
    let lineResult: boolean = $state(true);
    let id = page.params.id;
    let onlineUsers: string[] = $state([]);
    let enterRequest: enterProjectRequest = $state({
        username: "",
        enter: true,
    });
    let currentUser: AuthResponse = $state({
        id: -1,
        email: "",
        token: "",
        role: "",
        message: "",
    });
    let textareaRef: HTMLTextAreaElement;

    let updateMessage: UpdateCharacter = {
        userId: 0,
        index: 0,
        value: "",
    };
    let recievedMessage: UpdateCharacter = {
        userId: 0,
        index: 0,
        value: "",
    };
    let lockLine: lockLineRequest = $state({
        lineNumber: -1,
        userId: -1,
    });

    function getCurrentLine(): number {
        const position = textareaRef.selectionStart;
        const textBeforeCursor = textareaRef.value.substring(0, position);
        const lines = textBeforeCursor.split("\n");
        return lines.length;
    }
    function updateContent(char: string, index: number) {
        if (!textContent) textContent = "";
        if (char === "Backspace") {
            if (index > 0)
                textContent =
                    textContent.slice(0, index - 1) + textContent.slice(index);
        } else
            textContent =
                textContent.slice(0, index) + char + textContent.slice(index);

        setTimeout(() => {
            if (textareaRef) {
                const newPos =
                    char === "Backspace" ? Math.max(0, index - 1) : index + 1;
                textareaRef.selectionStart = newPos;
                textareaRef.selectionEnd = newPos;
            }
        }, 0);
    }

    function run(id: string) {
        let token = localStorage.getItem("token");
        client = new Client({
            webSocketFactory: () =>
                new SockJS(`http://localhost:9090/ws?token=${token}`, null, {
                    transports: ["websocket", "xhr-streaming"],
                }),

            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,

            onConnect: () => {
                client.subscribe(`/topic/project/${id}`, (message) => {
                    recievedMessage = JSON.parse(
                        message.body,
                    ) as UpdateCharacter;
                    updateContent(recievedMessage.value, recievedMessage.index);
                });
                client.subscribe(`/topic/project/${id}/chat`, (message) => {
                    messages.push(JSON.parse(message.body) as Message);
                    localStorage.setItem(
                        "chatMessages" + id,
                        JSON.stringify(messages),
                    );
                });
                client.subscribe(
                    `/topic/project/${id}/${currentUser.id}/cursor`,
                    (message) => {
                        lineResult = JSON.parse(message.body) as boolean;
                        if (!lineResult) {
                            textareaRef.blur();
                        }
                    },
                );
                client.subscribe(`/topic/project/${id}/enter`, (message) => {
                    onlineUsers = JSON.parse(message.body) as string[];
                });

                client.publish({
                    destination: `/app/project/${id}/enter`,
                    body: JSON.stringify(enterRequest),
                });
            },
        });
        client.activate();
    }

    async function getCurrentSnapshot() {
        textContent = await getDefaultSnapshot(id);
    }

    onMount(() => {
        try {
            const storedUser = localStorage.getItem("CurrentUser");
            if (storedUser) {
                currentUser = JSON.parse(storedUser) as AuthResponse;
            }
        } catch (error) {
            console.log(error);
        }
        updateMessage.userId = currentUser.id;
        enterRequest.enter = true;
        enterRequest.username = currentUser.email;
        getCurrentSnapshot();
        run(page.params.id);
    });

    onDestroy(() => {
        if (client) {
            client.deactivate();
            console.log("Disconnected from STOMP broker");
        }
    });
    function sendMessage(event: KeyboardEvent) {
        event.preventDefault();
        updateMessage.userId = currentUser.id;
        updateMessage.index = textareaRef.selectionStart;
        updateMessage.value = event.key;
        if (updateMessage.index % 35 == 0 && updateMessage.value == " ") {
            updateMessage.value = "\n";
        }
        if (event.key === "Enter") {
            updateMessage.value = "\n";
            handleClickCursor(1);
        }
        client.publish({
            destination: `/app/project/${id}`,
            body: JSON.stringify(updateMessage),
        });
    }

    function sendMessageToChat() {
        const chatMessage: Message = {
            id: -1,
            username: "",
            message: messageContent,
            date: Date.now().toString(),
        };
        client.publish({
            destination: `/app/project/${id}/chat`,
            body: JSON.stringify(chatMessage),
        });
        messageContent = "";
    }

    async function handleKeydown(event: KeyboardEvent) {
        let id = page.params.id;
        updateMessage.index = textareaRef.selectionStart;
        updateMessage.value = event.key;
        if (event.key === "Backspace") {
            event.preventDefault();
            client.publish({
                destination: `/app/project/${id}`,
                body: JSON.stringify(updateMessage),
            });
        }

        if (event.key.startsWith("Arrow")) {
            lockLine.lineNumber = getCurrentLine();
            lockLine.userId = currentUser.id;
            console.log(JSON.stringify(lockLine));

            client.publish({
                destination: `/app/project/${id}/${currentUser.id}/cursor`,
                body: JSON.stringify(lockLine),
            });
        }
    }
    async function handleClickCursor(b: number = 0) {
        let id = page.params.id;
        updateMessage.index = textareaRef.selectionStart;
        lockLine.lineNumber = getCurrentLine() + b;
        lockLine.userId = currentUser.id;
        console.log(JSON.stringify(lockLine));

        client.publish({
            destination: `/app/project/${id}/${currentUser.id}/cursor`,
            body: JSON.stringify(lockLine),
        });
    }

    async function takeSnapShot() {
        let id = page.params.id;
        var num: number = +id;
        const snapshot: Snapshot = {
            projectId: num,
            code: textContent,
            comments: JSON.stringify(messages),
        };
        console.log(JSON.stringify(snapshot));
        await saveSnapshot(snapshot);
    }
    function leavearea() {
        enterRequest.enter = false;
        client.publish({
            destination: `/app/project/${id}/enter`,
            body: JSON.stringify(enterRequest),
        });
        window.history.back();
    }
</script>

<div id="all_page">
    <div id="code_area">
        <textarea
            bind:this={textareaRef}
            onkeypress={sendMessage}
            onkeydown={handleKeydown}
            onclick={() => handleClickCursor(0)}
            rows="35"
            cols="35"
            wrap="hard">{textContent}</textarea
        >
        <br />
        <button onclick={takeSnapShot}>take snapshot</button>
        <button onclick={leavearea}>leave area</button>
    </div>
    <div id="chat_area">
        <div id="chat_content">
            <div id="message">
                {#each messages as message}
                    <p
                        style="background-color:{currentUser.id === message.id
                            ? '#90EE90'
                            : '#d3d3d3'}"
                    >
                        <b><span> {message.username.substring(0, 10)}</span></b>
                        <span> {message.message}</span>
                        <span style="font-size: 10px;"
                            >{new Date(
                                Number(message.date),
                            ).toLocaleTimeString()}</span
                        >
                    </p>
                {/each}
            </div>
        </div>
        <input
            type="text"
            bind:value={messageContent}
            onkeydown={(e) => e.key === "Enter" && sendMessageToChat()}
        />
        <button onclick={sendMessageToChat}>send</button>
    </div>
</div>
{#each onlineUsers as user}
    <p>{user}</p>
{/each}

<style>
    #all_page {
        display: flex;
        gap: 2rem;
        padding: 2rem;
        font-family: Arial, sans-serif;
        background-color: #f9f9f9;
    }

    #code_area {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
    }

    textarea {
        resize: none;
        width: 500px;
        height: 600px;
        padding: 1rem;
        font-family: "Courier New", monospace;
        font-size: 1rem;
        border: 1px solid #ccc;
        border-radius: 0.5rem;
        overflow-y: scroll;
        white-space: pre-wrap;
        overflow-wrap: break-word;
        background-color: #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    #code_area button {
        margin-top: 1rem;
        margin-right: 1rem;
        padding: 0.6rem 1.2rem;
        font-size: 1rem;
        border: none;
        border-radius: 0.4rem;
        background-color: #4caf50;
        color: white;
        cursor: pointer;
        transition: 0.2s ease-in-out;
    }

    #code_area button:hover {
        background-color: #43a047;
    }

    #chat_area {
        display: flex;
        flex-direction: column;
        width: 300px;
        height: 620px;
        border: 1px solid #ccc;
        border-radius: 0.5rem;
        background-color: #fff;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    #chat_content {
        flex: 1;
        overflow-y: auto;
        padding: 1rem;
        border-bottom: 1px solid #eee;
    }

    #message p {
        margin: 0.3rem 0;
        padding: 0.5rem;
        background-color: #f1f1f1;
        border-radius: 0.4rem;
        font-size: 0.9rem;
        word-wrap: break-word;
    }

    #chat_area input[type="text"] {
        padding: 0.5rem;
        font-size: 1rem;
        border: none;
        border-top: 1px solid #ddd;
        outline: none;
    }

    #chat_area button {
        padding: 0.5rem;
        font-size: 1rem;
        border: none;
        background-color: #2196f3;
        color: white;
        cursor: pointer;
    }

    #chat_area button:hover {
        background-color: #1976d2;
    }
</style>
