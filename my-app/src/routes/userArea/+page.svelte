<script lang="ts">
    import { addLanguage, getAllLanguages } from "$lib/api/languages";
    import {
        getAllProjects,
        addProject,
        deleteProject,
        setDefualtSnapshot,
    } from "$lib/api/project";
    import {
        deleteSnapshotById,
        getSnapshotsById,
        runSnapshoCode,
        updateSnapshotContent,
    } from "$lib/api/snapshot";
    import type {
        Project,
        ProjectRequest,
        updateSnapshotRequest,
        GetSnapshot,
        Languages,
        Snapshot,
        Message,
        RunCodeRequest,
    } from "$lib/models";
    import type { AuthResponse } from "$lib/store/user";
    import { onMount } from "svelte";
    let codeRunResults: string = $state("");
    let newProgrammingLanguage: string = $state("");
    let allRecivedLanguages: Languages[] = $state([]);
    let projectRequest: ProjectRequest = $state({
        name: "",
        language: "",
    });
    let snapshots: GetSnapshot[] = $state([]);
    let selectedSnapshot: GetSnapshot = $state({
        id: 0,
        code: "",
        projectId: 0,
        userId: 0,
        creationDate: "",
        comments: "",
    });
    let currentUser: AuthResponse = $state({
        id: 0,
        email: "",
        role: "",
        token: "",
        message: "",
    });
    let updatedContent: String = $state("");

    let projects: Project[] = $state([]);

    let currentProject: Project = $state({
        id: -1,
        userId: -1,
        snapshotId: -1,
        name: "null",
        language: "null",
        creationDate: "null",
    });
    let snapshotToRun: RunCodeRequest = $state({
        snapshotId: -1,
        code: "",
        args: "",
        language: "",
    });

    onMount(() => {
        try {
            const storedUser = localStorage.getItem("CurrentUser");
            if (storedUser) {
                currentUser = JSON.parse(storedUser) as AuthResponse;
            }
        } catch (error) {}
    });

    async function getAll() {
        projects = await getAllProjects();
    }

    async function saveProject(event: Event) {
        event.preventDefault();
        addProject(projectRequest);
    }

    async function getSnapshots(project: Project) {
        currentProject = project;
        snapshots = await getSnapshotsById(project.id);
        console.log(snapshots);
    }

    function redirectToProject(id: number) {
        window.location.href = `/projectArea/${id}`;
    }
    async function updateSnapshot() {
        const updateSnapshotRequest: updateSnapshotRequest = {
            snapshotId: selectedSnapshot.id,
            code: updatedContent.toString(),
        };
        updateSnapshotContent(updateSnapshotRequest);
    }
    async function runCode() {
        snapshotToRun.code = String(updatedContent);
        snapshotToRun.snapshotId = selectedSnapshot.id;
        snapshotToRun.language = currentProject.language;
        console.log(JSON.stringify(snapshotToRun));
        codeRunResults = await runSnapshoCode(snapshotToRun);
    }
    async function saveLanguage() {
        addLanguage(newProgrammingLanguage);
    }
    async function getLanguages() {
        allRecivedLanguages = await getAllLanguages();
    }

    async function updateDefaultProject(snapshotId: number) {
        setDefualtSnapshot(snapshotId);
    }
    async function deleteSnapshot(snapshotId: number) {
        await deleteSnapshotById(snapshotId);
    }
    async function deleteChosenProject(project: Project) {
        await deleteProject(project);
    }
    function logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("CurrentUser");
        window.location.href = "/auth";
    }
</script>

<div id="main_buttons">
    <button onclick={getAll}>get all projects</button>
    <button onclick={logout}>log out</button>
</div>
{#each projects as project}
    <div id="project">
        <span id="project_name">{project.name}</span>
        <button onclick={() => redirectToProject(project.id)}>Choose</button>
        <button onclick={() => getSnapshots(project)}>Show Snapshots</button>
        {#if currentUser.role === "Admin"}
            <button onclick={() => deleteChosenProject(project)}
                >delete project</button
            >
        {/if}
    </div>
{/each}
{#each snapshots as snapshot}
    {#if snapshot.id !== 0 && snapshot.code !== null}
        <div
            id="snapshot"
            style="background-color: {snapshot.id === currentProject.snapshotId
                ? '#90EE90'
                : '#d3d3d3'}"
        >
            <span>{snapshot.id}</span>
            <span>{snapshot.creationDate}</span>

            <button
                onclick={() =>
                    (updatedContent = (selectedSnapshot = snapshot).code)}
                >Choose</button
            >
            {#if currentUser.role === "Admin"}
                <button
                    id="set_button"
                    onclick={() => updateDefaultProject(snapshot.id)}
                    >Set</button
                >
                <button
                    id="delete_button"
                    onclick={() => deleteSnapshot(snapshot.id)}>Delete</button
                >
            {/if}
        </div>
    {/if}
{/each}
<div id="single_snap">
    {#if selectedSnapshot.id !== 0 && selectedSnapshot.code !== null}
        <div>
            <textarea
                name=""
                id=""
                rows="35"
                cols="35"
                disabled={currentUser.role == "Admin"}
                bind:value={updatedContent}
                >{updatedContent}
            </textarea><br />
            <textarea name="" cols="35" id="" bind:value={snapshotToRun.args}
            ></textarea><br />
            {#if currentUser.role === "User"}
                <button onclick={updateSnapshot}>Update Snapshot</button>
            {/if}
            <button onclick={runCode}>Run Code</button>
        </div>
        <div>
            <textarea
                name=""
                id=""
                rows="35"
                cols="35"
                disabled
                bind:value={codeRunResults}
            ></textarea>
        </div>
        <div id="chat_area">
            <div id="chat_content">
                <div id="message">
                    {#each JSON.parse(selectedSnapshot.comments) as Message[] as message}
                        <p
                            style="background-color:{currentUser.id ===
                            message.id
                                ? '#90EE90'
                                : '#d3d3d3'}"
                        >
                            <b
                                ><span>
                                    {message.username.substring(0, 10)}</span
                                ></b
                            >
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
        </div>
    {/if}
</div>
<div class="addForm">
    <form action="POST">
        project name <input type="text" bind:value={projectRequest.name} /><br
        />
        project programming language
        <select
            name=""
            id=""
            onclick={getLanguages}
            bind:value={projectRequest.language}
        >
            {#each allRecivedLanguages as language}
                <option value={language.language}>{language.language}</option>
            {/each}
        </select>

        <button onclick={saveProject}>save my project</button>
    </form>
</div>
{#if currentUser.role === "Admin"}
    <div class="addForm">
        <form action="">
            New Language <input
                type="text"
                name=""
                id=""
                bind:value={newProgrammingLanguage}
            />
            <button onclick={saveLanguage}>save new language</button>
        </form>
    </div>
{/if}

<style>
    #single_snap {
        display: flex;
    }
    /* h1 {
        font-size: 24px;
        font-weight: bold;
        margin-bottom: 1rem;
    } */
    #main_buttons {
        display: flex;
        justify-content: space-between;
    }
    button {
        background-color: #4f46e5;
        color: white;
        border: none;
        padding: 0.5rem 1rem;
        margin: 0.25rem;
        border-radius: 0.375rem;
        cursor: pointer;
        transition: background-color 0.2s ease-in-out;
    }

    button:hover {
        background-color: #3730a3;
    }

    #project {
        background-color: #f9fafb;
        border: 1px solid #e5e7eb;
        padding: 1rem;
        border-radius: 0.5rem;
        margin: 0.5rem 0;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    #snapshot {
        background-color: #d3d3d3;
        border: 1px solid #e5e7eb;
        padding: 1rem;
        border-radius: 0.5rem;
        margin: 0.5rem 0;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    #project span,
    #snapshot span {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: 500;
    }

    textarea {
        font-family: monospace;
        font-size: 1rem;
        border: 1px solid #d1d5db;
        border-radius: 0.5rem;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        margin-top: 1rem;
        margin-bottom: 0.5rem;
        resize: none;
    }

    .addForm {
        margin-top: 2rem;
        padding: 1rem;
        background-color: #f3f4f6;
        border-radius: 0.5rem;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    }

    .addForm form {
        display: flex;
        flex-direction: column;
    }

    .addForm input {
        padding: 0.5rem;
        margin-bottom: 1rem;
        border: 1px solid #d1d5db;
        border-radius: 0.375rem;
    }
    .addForm select {
        padding: 0.5rem;
        margin-bottom: 1rem;
        border: 1px solid #d1d5db;
        border-radius: 0.375rem;
    }
    #chat_area {
        display: flex;
        flex-direction: column;
        width: 300px;
        height: 675px;
        border: 1px solid #ccc;
        border-radius: 0.5rem;
        background-color: #fff;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        margin-top: 1rem;
        margin-bottom: 0.5rem;
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
</style>
