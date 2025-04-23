import type { Project, ProjectRequest } from "$lib/models";
import { paths } from "$lib";

export async function getAllProjects(): Promise<Project[]> {
    const token = localStorage.getItem("token") || "";
    try {
        const res = await fetch(`http://localhost:9090/${paths.sharedArea}/project`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
        const data = await res.json();
        return data.message as Project[];
    } catch (error) {
        console.log(JSON.stringify(error));
        return [];
    }
}
export async function addProject(projectRequest: ProjectRequest) {
    try {
        const token = localStorage.getItem("token") || "";
        console.log(token);
        const res = await fetch(`http://localhost:9090/${paths.sharedArea}/project`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(projectRequest),
        });
    } catch (error) {
        console.log(error);
    }
}

export async function deleteProject(project: Project) {
    try {
        const token = localStorage.getItem("token") || "";

        let projectId = project.id;
        const res = await fetch(`http://localhost:9090/${paths.adminArea}/project/${projectId}`, {
            method: "Delete",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        });
    } catch (error) {
        console.log(error);
    }
}
export async function setDefualtSnapshot(snapshotId: number) {
    try {
        const token = localStorage.getItem("token") || "";
        const res = await fetch(
            `http://localhost:9090/${paths.adminArea}/snapshot/default/${snapshotId}`,
            {
                method: "PUT",
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
