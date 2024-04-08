<template>
    <div class="landing-page">

        <section class="hero">
            <div class="hero-content">
                <div class="hero-text-and-button">
                    <div class="hero-text">
                        <h1>Later Ladder</h1>
                        <p class="tagline">Enjoy footy on your schedule - spoiler free.</p>
                    </div>
                    <div class="hero-button-container">
                        <router-link to="/guest-dashboard" class="cta-button hero-button">Try it out as a
                            guest</router-link>
                    </div>
                </div>
                <div class="hero-image">
                    <img src="@/assets/laptop-mockup-straight.svg" alt="Later Ladder laptop mockup"
                        class="laptop-mockup">
                </div>
            </div>
        </section>

        <section class="mission">
            <div class="mission-content">
                <h2>Our Mission</h2>
                <div class="mission-text-box">
                    <p>Our mission at Later Ladder is straightforward: to offer a spoiler-free experience for AFL fans.
                        We believe that every fan deserves to enjoy the game's outcomes and the evolving team standings
                        without the risk of spoilers. Our solution is a personalized, spoiler-free zone for AFL content,
                        tailored to your viewing schedule.</p>
                </div>
            </div>
        </section>

        <section class="features">
            <h2>Features</h2>
            <div class="feature-cards">
                <div class="feature-card">
                    <img src="@/assets/feature1.svg"
                        alt="Vector illustration of a human figure responding to an alert issued by a mobile device"
                        class="feature-icon">
                    <div class="feature-text">
                        <h3>Control What You See</h3>
                        <p>Mark games as watched and see team standings update accordingly. You're in control, free from
                            worrying about spoilers for the games you haven't caught up on yet.</p>
                    </div>
                </div>
                <div class="feature-card">
                    <img src="@/assets/feature2.svg"
                        alt="Vector illustration of a human figure selecting from presented options" class="feature-icon">
                    <div class="feature-text">
                        <h3>Personalize Your AFL Experience</h3>
                        <p>With a simple account setup, your watched games list and the corresponding ladder updates are
                            saved
                            and customized to your viewing journey.</p>
                    </div>
                </div>
                <div class="feature-card">
                    <img src="@/assets/feature3.svg"
                        alt="Vector illustration of a human figure sitting by a large clock, adjusting the time displayed by the clockhands"
                        class="feature-icon">
                    <div class="feature-text">
                        <h3>Enjoy AFL on Your Terms</h3>
                        <p>Our app is built for ease and efficiency, focusing on delivering the information you want,
                            precisely
                            when you want it.</p>
                    </div>
                </div>
            </div>
        </section>

        <div class="why-us-container">
            <section class="why-us">
                <h2 class="why-us-header">Why Choose Later Ladder?</h2>
                <div class="phone-mockup">
                    <img src="@/assets/phone-mockup-angled.svg" alt="Later Ladder phone mockup">
                </div>
                <div class="why-us-content">  
                    <p>Later Ladder was created from a genuine passion for AFL and an understanding of the international
                        fan's dilemma. We bridge the gap between your love for AFL and the practicality of your personal
                        schedule. Our app ensures that you never have to compromise on the excitement of the game due to
                        spoilers again.</p>
                    <p>At Later Ladder, we're not just another app; we're your essential tool for enjoying AFL games on your
                        schedule, keeping the spirit of competition alive and thrilling. Dive into your games, manage your
                        standings, and enjoy AFL like never before, all through a straightforward, user-friendly platform.
                    </p>
                </div>
            </section>
        </div>

        <section class="cta">
            <div class="cta-text">
                <h2>Embrace each match at your pace, without spoilers.</h2>
            </div>
            <div class="get-started-button-container">
                <router-link to="/register" class="cta-button get-started-button">Get Started</router-link>
            </div>
        </section>
    </div>
</template>

<script>
export default {
    name: 'LandingPage',
}
</script>

<style scoped>
.landing-page {
    margin: -2rem auto 0;
    background-color: var(--afl-800);
}

.hero {
    display: flex;
    background: linear-gradient(var(--afl-800), var(--afl-600));
    padding: 4rem 0;
    justify-content: center;
}

.hero-content {
    max-width: 1200px;
    display: flex;
    margin: 2rem 4rem 4rem;
    align-items: center;
    justify-content: space-between;
}

.hero-text-and-button {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding-bottom: 4rem;
}

h1 {
    font-size: 4.5rem;
    text-transform: uppercase;
    margin-bottom: 1rem;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.hero-text h1 {
    font-weight: 700;
    line-height: 1.25;
}

.tagline {
    font-size: 1.75rem;
    margin-bottom: 2.5rem;
}

.hero-image {
    text-align: right;
    max-width: 40%;
}

.cta-button {
    background-color: var(--green);
    color: var(--afl-100);
    padding: 1rem 2rem;
    border-radius: 8px;
    text-decoration: none;
    font-size: 1.25rem;
    font-weight: 600;
    transition: background-color 0.3s ease;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.cta-button:hover {
    background-color: var(--green-bright);
}

.mission {
    display: flex;
    justify-content: center;
    padding: 4rem 10rem;
    text-align: center;
}

.mission-content {
    max-width: 1200px;
    background: linear-gradient(var(--afl-800), var(--afl-600) 120%);
    border: 4px dashed var(--green-bright);
    border-radius: 6rem;
    margin: 1rem 6rem;
    padding: 1rem 6rem;
}

.mission-text-box p {
    padding: 1rem;
    font-size: 1.4rem;
    text-align: left;
}

h2 {
    font-size: 3rem;
    margin-bottom: 1.25rem;
    color: var(--green-bright);
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.features {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    margin: 2rem auto 4rem;
}

.features h2 {
    text-decoration: underline;
    text-decoration-color: var(--afl-500);
    text-decoration-thickness: 2px;
}

.feature-cards {
    display: flex;
    justify-content: space-between;
    max-width: 1200px;
}

.feature-card {
    max-width: 360px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex: 1;
    margin: 2rem 1rem;
    padding: 1rem 2rem 2rem 2rem;
    background: linear-gradient(0deg, var(--afl-600) 30%, var(--afl-800) 100%);
    border: 2px solid var(--afl-600);
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.feature-icon {
    padding: 3rem 0 1rem;
    height: 100px;
    width: auto;
    align-self: center;
}

.feature-text p {
    font-size: 1.2rem;
}

h3 {
    font-size: 1.75rem;
    margin-bottom: 1rem;
    color: var(--afl-400);
    text-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
}

.why-us-container {
    display: flex;
    justify-content: center;
    background-color: var(--afl-600);
}

.why-us {
    max-width: 1200px;
    width: 100%;
    padding: 4rem;
    display: grid;
    grid-template-columns: minmax(auto, 25%) 1fr;
    grid-template-areas:
        "image header"
        "image text";
    gap: 0 1rem;
    align-items: center;
}

.phone-mockup {
    grid-area: image;
    align-self: center;
    max-width: 256px;
}

.why-us-header {
    grid-area: header;
    margin: 2rem 0 1rem 2rem;
}

.why-us-content {
    grid-area: text;
    font-size: 1.2rem;
    margin: 0 2rem;
    max-width: 1000px;
}

.cta {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: var(--afl-800);
    text-align: center;
    margin: 0 0 -4rem 0;
    padding: 4rem 4rem 8rem 4rem;
    border-bottom: 1px solid var(--afl-700);
}

.cta-text {
    max-width: 1000px;
    background-color: var(--afl-600);
    padding: 1rem 4rem;
    margin: 0 10rem;
    border-radius: 8rem;
}

.cta-text h2 {
    color: var(--afl-200);
    font-size: 2.25rem;
    font-weight: 600;
}

.get-started-button-container {
    margin-top: 2.5rem;
}

@media (max-width: 1200px) {

    .hero-content {
        margin: 2.5rem 4rem 4rem;
    }

    .hero-text h1 {
        font-size: 3.75rem;
        line-height: 1.1;
        padding: 0 0 1rem 0;
    }

    .tagline {
        font-size: 1.5rem;
        margin-bottom: 2.5rem;
    }

    .mission {
        padding: 3rem 4rem;
    }

    .mission-content {
        padding: 0 3rem 1rem;
    }

    .feature-cards {
        margin: 1rem 1rem 3rem 1rem;
    }

    .feature-card {
        margin: 0.5rem;
        padding: 1rem;
    }

    .feature-text p {
        font-size: 1rem;
    }

    .cta {
        padding: 4rem 2rem 8rem 2rem;
    }

    .cta-text {
        padding: 1rem 6rem;
    }
}

@media (max-width: 1024px) {

    .hero {
        padding: 3.5rem 0;
    }

    .hero-content {
        margin: 0 1rem;
        padding: 0 0.5rem 2rem 0.5rem;
    }

    .hero-text h1 {
        font-size: 3.5rem;
        padding: 0;
        margin: 0;
    }

    .tagline {
        font-size: 1.5rem;
        padding: 1rem 0 0.5rem 0;
    }

    .hero-image {
        max-width: 45%;
        padding-left: 0.75rem;
    }

    .mission {
        margin: 0;
    }

    .mission-content {
        margin: 0;
    }

    .features h2 {
        margin-bottom: 0.5rem;
    }

    .feature-cards {
        margin: 2rem 0;
    }

    .feature-card {
        padding: 1rem;
    }

    .feature-icon {
        padding-bottom: 0;
    }

    .why-us {
        padding: 2rem;
    }

    .cta {
        padding: 3rem 0 8rem 0;
    }

    .cta-text {
        padding: 1rem;
        margin: 1rem 4rem;
    }

}

@media (max-width: 768px) {

    .hero {
        padding: 2rem 0;
    }

    .hero-content {
        max-width: 80%;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .hero-text {
        order: 1;
        text-align: center;
        margin: 0.5rem 0.5rem 0 0.5rem;
        padding: 0;
    }

    .hero-text h1 {
        margin: 2.5rem auto;
    }

    .tagline {
        text-align: center;
        margin-bottom: 1rem;
        padding-top: 0;
    }

    .hero-image {
        order: 2;
        text-align: center;
        max-width: calc(100% - 4rem);
        margin: 1rem 0 0 0;
        padding-left: 0;
    }

    .hero-button-container {
        order: 3;
        margin-top: 1.5rem;
        text-align: center;
    }

    h2 {
        font-size: 2.5rem;
    }

    h3 {
        font-size: 1.75rem;
    }

    .mission,
    .why-us {
        padding: 0;
    }

    .mission-content {
        background-image: none;
        background-color: var(--afl-800);
        border-radius: 0;
        border-top: 4px dashed var(--green-bright);
        border-bottom: 4px dashed var(--green-bright);
        border-left: none;
        border-right: none;
        margin: 0;
        padding: 2rem 1rem;
    }

    .mission-content p {
        font-size: 1.1rem;
        text-align: left;
        padding: 0 1rem 1.5rem 1rem;
    }

    .features h2 {
        margin: 2rem 0 0 0;
        text-decoration: none;
    }

    .feature-cards {
        flex-direction: column;
        align-items: center;
        margin-top: 1rem;
        padding: 1rem;
    }

    .feature-card {
        margin: 1rem;
    }

    .feature-icon {
        max-width: 55%;
        height: auto;
        margin: 0.5rem auto;
    }

    .feature-text {
        padding: 0.5rem 1rem 1.5rem 1rem;
    }

    .feature-text p {
        font-size: 1.1rem;
    }

    .feature-cards h3 {
        text-align: center;
        padding-bottom: 0.5rem;
    }

    .why-us {
        padding: 2rem 0.5rem;
        grid-template-columns: minmax(0, 40%) 1fr;
        grid-template-rows: auto auto;
        gap: 0;
    }

    .phone-mockup {
        grid-column: 1 / 2;
        grid-row: 1 / 2;
        max-width: 75%;
        justify-self: right;
        padding-right: 1.5rem;
        border-right: 1px solid var(--green);
    }

    .why-us-header {
        font-size: 3rem;
        grid-column: 2 / 3;
        grid-row: 1 / 2;
        align-self: center;
        max-width: 60%;
        line-height: 1.25;
    }

    .why-us-content {
        grid-column: 1 / -1;
        grid-row: 2 / 3;
        margin-top: 1rem;
        font-size: 1rem;
    }

    .cta {
        padding-top: 2rem;
    }

    .cta-text {
        padding: 1rem;
        margin: 0.5rem 2rem;
    }

    .cta-text h2 {
        font-size: 2rem;
    }
}

@media (max-width: 480px) {

    .mission-content {
        padding: 1rem;
    }

    .feature-cards {
        margin-top: 1.5rem;
    }

    .feature-card {
        margin: 0.5rem 0.25rem;
    }

    .why-us-header {
        font-size: 2.5rem;
    }

    .why-us-content {
        margin: 0.5rem;
    }

    .cta-text {
        padding: 0.25rem 0.5rem;
        margin: 0.5rem;
    }

    .cta-text h2 {
        font-size: 1.5rem;
    }
}
</style>