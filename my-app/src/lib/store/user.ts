import { writable } from "svelte/store";



export interface AuthResponse {
    id: number;
    email: string;
    token: string;
    role: string;
    message: string;
}
export const currentUser = writable<AuthResponse | null>(null);
