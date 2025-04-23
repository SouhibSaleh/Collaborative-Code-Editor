import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { writable, type Writable } from "svelte/store";
let client: Client;

export function run(id: string) {

    const connectHeaders = {
        Authorization: `Bearer ${localStorage.getItem("token")}`
    };
    client = new Client({
        webSocketFactory: () => new SockJS("http://localhost:9090/ws", null, {
            transports: ['websocket', 'xhr-streaming']
        }),
        connectHeaders: connectHeaders,
        debug: (str) => console.log("[STOMP]", str),

        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,

        onConnect: () => {
            console.log("Connected to STOMP broker");

            client.subscribe(`/topic/project/${id}`, (message) => {
                console.log("Received message:", message.body);
            });

            client.publish({
                destination: `/app/project/${id}`,
                body: JSON.stringify({ content: "connection noice" }),
            });
        },

        onStompError: (frame) => {
            console.error("STOMP protocol error", frame.headers.message);
        },

        onWebSocketError: (error) => {
            console.error("WebSocket error", error);
        },

        onWebSocketClose: () => {
            console.log("WebSocket connection closed");
        },
    });

    client.activate();
}
export function sendMessage(message: string, id: string) {
    client.publish({
        destination: `/app/project/${id}`,
        body: JSON.stringify({ content: message }),
    });
}
export function getMessages(id: string) {
    const messages = writable<string[]>([]);
    client.subscribe(`/topic/project/${id}`, (message) => {
        messages.update(current => [...current, message.body]);

    });
    return messages;

}
export function disconnect() {
    if (client) {
        client.deactivate();
        console.log("Disconnected from STOMP broker");
    }
}
