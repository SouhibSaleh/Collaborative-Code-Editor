<script lang="ts">
    import { onMount } from "svelte";
    import type { AuthResponse } from "$lib/store/user";
    import { currentUser } from "$lib/store/user";
    import { get } from "svelte/store";
    import { json } from "@sveltejs/kit";
    import { paths } from "$lib";

    let { email = "", password = "", isAdmin = false } = $props();
    let formType = $state();
    let response: string = $state("");
    interface Request {
        email: string;
        password: string;
        isAdmin: boolean;
    }
    let payload: Request = {
        email: "",
        password: "",
        isAdmin: false,
    };

    async function handleCredentialResponse(response: any) {
        let token: string = response.credential;
        try {
            const res = await fetch(`http://localhost:9090/google`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(token),
            });
            const data = (await res.json()) as AuthResponse;
            if (data.message != null) {
                response = data.message;
            } else if (data.token) {
                localStorage.setItem("token", data.token);
                localStorage.setItem("CurrentUser", JSON.stringify(data));
                window.location.href = "/userArea";
            }
        } catch (error) {
            console.log(error);
        }
    }
    onMount(() => {
        window.google.accounts.id.initialize({
            client_id:
                "414586783427-cs7k85j8rlkbgnpfr199gesrrgfh1oiq.apps.googleusercontent.com",
            callback: handleCredentialResponse,
        });

        window.google.accounts.id.renderButton(
            document.getElementById("google-signin-btn")!,
            {
                theme: "outline",
                size: "large",
            },
        );
    });

    async function handleSubmit(event: Event) {
        event.preventDefault();
        if (formType === "google") {
            return;
        }

        payload.email = email;
        payload.password = password;
        payload.isAdmin = isAdmin;

        try {
            const res = await fetch("http://localhost:9090/" + formType, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });
            const data = (await res.json()) as AuthResponse;

            if (formType === "login" && data.token != null) {
                localStorage.setItem("CurrentUser", JSON.stringify(data));
                localStorage.setItem("token", data.token);
                if (isAdmin) {
                    window.location.href = "/adminArea";
                } else {
                    window.location.href = "/userArea";
                }
            }
        } catch (error) {
            response = "Error submitting form";
            console.error(error);
        }
    }
</script>

<form method="POST" onsubmit={handleSubmit}>
    email: <input type="text" bind:value={email} /><br />
    password: <input type="password" bind:value={password} /><br />
    admin? <input type="checkbox" bind:checked={isAdmin} /><br />
    <button type="submit" onclick={() => (formType = "login")}>Login</button>
    <button type="submit" onclick={() => (formType = "signup")}>Signup</button>
    <div id="google-signin-btn"></div>
</form>

<p>
    {response}
</p>

<style>
    form {
        max-width: 400px;
        margin: 2rem auto;
        padding: 2rem;
        border: 1px solid #ddd;
        border-radius: 1rem;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        background-color: #fafafa;
        font-family: sans-serif;
    }
    form button {
        width: 100%;
    }
    input {
        display: block;
        width: 100%;
        margin-bottom: 1rem;
    }

    input[type="text"],
    input[type="password"] {
        padding: 0.5rem;
        border: 1px solid #ccc;
        border-radius: 0.5rem;
        font-size: 1rem;
    }

    input[type="checkbox"] {
        width: auto;
        margin-right: 0.5rem;
    }

    button {
        width: 48%;
        margin: 0.5rem 1%;
        padding: 0.6rem;
        font-size: 1rem;
        border: none;
        border-radius: 0.5rem;
        background-color: #4caf50;
        color: white;
        cursor: pointer;
    }

    button:hover {
        background-color: #45a049;
    }

    #google-signin-btn {
        margin-left: 25%;
        margin-top: 5%;
        width: 100%;
    }

    p {
        text-align: center;
        color: #e53935;
        font-weight: bold;
        margin-top: 1rem;
    }
</style>
