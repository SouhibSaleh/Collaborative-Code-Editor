import type { Languages } from "$lib/models";
import { json } from "@sveltejs/kit";
import { paths } from "$lib";

export async function addLanguage(language: string) {
    const token = localStorage.getItem("token") || "";
    console.log(`http://localhost:9090/${paths.adminArea}/languages`);
    try {
        const res = await fetch(`http://localhost:9090/adminArea/languages`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: language
        });
    } catch (error) {
        console.log(JSON.stringify(error));
        return [];
    }
}
export async function getAllLanguages(): Promise<Languages[]> {
    const token = localStorage.getItem("token") || "";

    try {
        const res = await fetch(`http://localhost:9090/${paths.sharedArea}/languages`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
        let data = await res.json();
        return data as Languages[];
    } catch (error) {
        console.log(JSON.stringify(error));
        return [];
    }
}