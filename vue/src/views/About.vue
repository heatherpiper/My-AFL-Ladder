<template>
    <div class="about-container">
        <div class="about-content">
            <div class="about-later-ladder">
                <h2>About Later Ladder</h2>
                <p>Later Ladder is a web application tailored for fans of the Australian Football League (AFL), which enables users to track AFL team standings in a unique way. Designed for those who prefer watching games after their live broadcast, it allows for the updating and viewing of team standings based on the outcomes of games the user has marked as watched, without spoiling the results of games yet to be seen. This project was born from my personal experience as an American AFL fan, addressing the challenge of following the league from a timezone where games are often aired overnight.</p>
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
            <div class="about-the-developer">
                <h2>About the developer</h2>
                <p>I'm Heather Piper, a full-stack developer from Columbus, Ohio. Prior to being a developer, I worked as a court reporter and ran a successful 3D printing business that fulfilled nearly 1,000 online orders over three years. I graduated from Tech Elevator's Java bootcamp in September 2023.</p>
                <h3>Contact me</h3>
                <p>I'm very open to feedback and suggestions of any sort. Please feel free to reach out to me on my <a href="https://github.com/heatherpiper">Github profile</a> or email me at <a href="mailto:heather.a.piper@gmail.com">heather.a.piper@gmail.com</a></p>
            </div>
            <div class="button-container">
                <router-link class="back-button" to="/">Back to main page</router-link>
            </div>
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
    margin: 2em auto;
    padding: 1.8em;
    background-color: var(--afl-200);
    border: 1px solid var(--afl-800);
    border-radius: 12px;
    box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);
}

h2, h3 {
    color: var(--afl-600);
}

p, li {
    color: var(--afl-900);
}

a {
    color: var(--afl-500);
}

a:visited {
    color: var(--afl-500);
}

.button-container {
    display: flex;
    justify-content: center;
    width: 100%;
}

.back-button {
    display: inline-block;
    margin: 20px;
    padding: 16px 32px;
    border-radius: 8px;
    background-color: var(--afl-500);
    color: var(--afl-200);
    cursor: pointer;
    font-size: 16px;
    text-decoration: none;
    text-align: center;
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
        max-width: 90%;
        padding: 16px;
    }

    ul {
        padding-left: 1em;
    }

    .back-button {
        padding: 12px 32px;
    }
}


</style>