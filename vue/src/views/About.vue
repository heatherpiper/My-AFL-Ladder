<template>
    <div class="about-container">

        <div class="section-container">
            <div class="header-box">
                <h2 class="section-header">About Later Ladder</h2>
            </div>
            <div class="section-box">
                <div class="section-content">
                    <div class="logo-container">
                        <img src="@/assets/LaterLadder-Logo.webp" alt="Later Ladder color logo" class="logo-image">
                    </div>
                    <p>Later Ladder is a web application tailored for fans of the Australian Football League (AFL), which enables users to track AFL team standings in a unique way. Designed for those who prefer watching games after their live broadcast, it allows for the updating and viewing of team standings based on the outcomes of games the user has marked as watched, removing the risk of spoiling the outcomes of games not yet seen. This project was born from my personal experience as an American AFL fan, addressing the challenge of following the league from a timezone where games are often aired overnight.</p>
                    <h3>Core technologies</h3>
                    <ul>
                        <li><strong>Backend</strong>: Utilizes Java and Spring Boot, creating a robust API for data processing and interaction</li>
                        <li><strong>Database</strong>: Employs PostgreSQL for secure and efficient data storage</li>
                        <li><strong>Frontend interface</strong>: Developed with Vue.js to offer a responsive and user-friendly experience, allowing live updates without page reloads</li>
                        <li><strong>Data integration</strong>: Game data is fetched from the <a href="https://squiggle.com.au/">Squiggle</a> API, enabling users to access accurate game data and real-time updates</li>
                    </ul>
                    <h3>Recent updates</h3>
                    <ul>
                        <li><strong>Real-time game data</strong>: Integration of server-sent events (SSEs) from the Squiggle API now allows for instant updates as games conclude, letting users immediately mark games as watched</li>
                        <li><strong>Guest usage</strong>: Redesigned backend logic in JavaScript to enable usage of Later Ladder without an account, leveraging the browser's session storage for data peristence</li>
                        <li><strong>Improved security</strong>: Implemented stricter password requirements to bolster user account security</li>
                        <li><strong>Session management</strong>: Added notifications for expired sessions with prompts to re-login.</li>
                    </ul>
                    <h3>Planned features</h3>
                    <ul>
                        <li>Ability to toggle scores on watched games on or off</li>
                        <li>Ability to mark all games in a round watched at once</li>
                        <li>Display wins, losses, and draws in ladder</li>
                        <li>Enhanced user accounts with options for password recovery and ability to log in using OAuth providers</li>
                    </ul>
                    
                </div>
            </div>
        </div>


        <div class="section-container">
            <div class="header-box">
                <h2 class="section-header">About the developer</h2>
            </div>
            <div class="section-box">
                <div class="section-content">
                    <p>I'm Heather Piper, a full-stack developer from Columbus, Ohio. Prior to being a developer, I worked as a court reporter and ran a successful 3D printing business that fulfilled nearly 1,000 online orders over three years. I graduated from Tech Elevator's Java bootcamp in September 2023.</p>
                    <h3>Contact me</h3>
                    <p>I welcome feedback and suggestions of all varieties. Feel free to reach out to me on my <a href="https://github.com/heatherpiper">Github profile</a> or email me at <a href="mailto:heather.a.piper@gmail.com">heather.a.piper@gmail.com</a></p>
                </div>
            </div>
        </div>

        <div class="button-container">
            <router-link class="back-button" to="/">Back to main page</router-link>
        </div>

    </div>
</template>

<script>
export default {
    name: 'About',
}
</script>

<style scoped>

.about-container {
    max-width: 80%;
    margin: auto;
}

.header-box {
    background-color: var(--afl-800);
    border-radius: 12px 12px 0 0;
    box-shadow: 8px 8px 8px rgba(0, 0, 0, 0.1);
    padding: 4px 12px;
}

.section-header {
    font-family: 'Roboto', sans-serif;
    font-size: xx-large;
    color: var(--afl-200);
    margin: 0;
    text-shadow: 1px 1px 2px var(--afl-900);
}

.section-content {
    background-color: var(--afl-200);
    border-radius: 0 0 12px 12px;
    margin-bottom: 1.5em;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding:  10px 24px 24px 24px;
    color: var(--afl-800);
    border: 1px solid var(--afl-800);
    line-height: 1.5em;
}

.logo-container {
    text-align: center;
    margin-top: 1em;
}

.logo-image {
    max-width: 400px;
}

.section-content p {
    margin-top: 0.75em;
}

.section-content h3 {
    color: var(--afl-500);
    font-size: x-large;
    margin-top: 1.5em;
    margin-bottom: 0;
}

.section-content li {
    margin: 0.75em 0;
}

.section-content li strong {
    color: var(--afl-600);
}

.section-content a {
    text-decoration: none;
    color: var(--afl-500);
}

.section-content a:hover {
    text-decoration: underline;
    cursor: pointer;
}

.button-container {
    text-align: center;
}

.back-button {
    display: inline-flex;
    margin: 1em auto;
    padding: 1em 2em;
    border: 1px solid var(--afl-600);
    border-radius: 8px;
    background-color: var(--afl-500);
    color: var(--afl-200);
    box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.1);
    cursor: pointer;
    text-decoration: none;
    transition: background-color 0.1s ease-in-out;
}

.back-button:hover {
    background-color: var(--afl-450);
}

.back-button:visited {
    color: var(--afl-200);
}

@media (max-width: 768px) {
    .about-container {
        max-width: 100%;
    }

    .section-content {
        padding: 10px 16px 16px 16px;
    
    }

    .section-content li strong {
        color: var(--afl-800);
    }

    ul {
        padding-left: 1em;
    }

    .logo-image {
        max-width: 300px;
    }

    .back-button {
        display: block;
        background-color: var(--afl-450);
    }
}


</style>