import type { GetSnapshot, RunCodeRequest, updateSnapshotRequest } from "$lib/models";
import { paths } from "$lib";
import type { Snapshot } from "$lib/models";


export async function getSnapshotsById(projectId: number): Promise<GetSnapshot[]> {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(
            `http://localhost:9090/${paths.sharedArea}/snapshot/${projectId}`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            },
        );
        const data = await res.json();
        return data as GetSnapshot[];
    } catch (error) {
        console.log(error);
        return []
    }

}
export async function updateSnapshotContent(updateSnapshotRequest: updateSnapshotRequest) {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(`http://localhost:9090/${paths.userArea}/snapshot`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(updateSnapshotRequest),
        });
        const data = await res.json();

    } catch (error) {
        console.log(error);
    }
}
export async function runSnapshoCode(snapshotToRun: RunCodeRequest): Promise<string> {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(`http://localhost:9090/${paths.sharedArea}/snapshot/run`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(snapshotToRun),
        });
        const data = await res.json();
        return data.message;
    } catch (error) {
        console.log(error);
    }
    return "";
}

export async function deleteSnapshotById(snapshotId: number) {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(
            `http://localhost:9090/${paths.adminArea}/snapshot/${snapshotId}`,
            {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            },
        );
    } catch (error) {
        console.log(error);
    }
}

export async function saveSnapshot(snapshot: Snapshot) {
    const token = localStorage.getItem("token") || "";
    
    try {
        const res = await fetch(`http://localhost:9090/${paths.sharedArea}/snapshot`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(snapshot),
        });
    } catch (error) {
        console.log(error);
    }

}
export async function getDefaultSnapshot(projectId: string): Promise<string> {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(
            `http://localhost:9090/${paths.sharedArea}/project/${projectId}/snapshot`,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            },
        );
        const data = (await res.json()) as Snapshot;
        return data.code;
    } catch (error) {
        console.log(error);
    }
    return ""
}