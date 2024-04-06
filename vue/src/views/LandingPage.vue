<template>
    <div class="landing-page">
        <section class="hero">
            <div class="hero-content">
                <div class="hero-text">
                    <h1>Later Ladder</h1>
                    <p class="tagline">Enjoy footy on your schedule - spoiler free.</p>
                    <router-link to="/guest-dashboard" class="cta-button">Try it out as a guest</router-link>
                </div>
                <div class="hero-image">
                    <img src="@/assets/laptop-mockup-straight.svg" alt="Later Ladder laptop mockup" class="laptop-mockup">
                </div>
            </div>
        </section>

        <section class="mission">
            <div class="mission-content">
                <h2>Our Mission</h2>
                <div class="mission-text-box">
                    <p>Our mission at Later Ladder is straightforward: to offer a spoiler-free experience for AFL fans. We believe that every fan deserves to enjoy the game's outcomes and the evolving team standings without the risk of spoilers. Our solution is a personalized, spoiler-free zone for AFL content, tailored to your viewing schedule.</p>
                </div>    
            </div>
        </section>

        <section class="features">
            <div class="feature-card">
                <img src="@/assets/feature1.svg" alt="Feature 1 icon" class="feature-icon">
                <h3>Control What You See</h3>
                <p>Mark games as watched and see team standings update accordingly. You're in control, free from worrying about spoilers for the games you haven't caught up on yet.</p>
            </div>
            <div class="feature-card">
                <img src="@/assets/feature2.svg" alt="Feature 2 icon" class="feature-icon">
                <h3>Personalize Your AFL Experience</h3>
                <p>With a simple account setup, your watched games list and the corresponding ladder updates are saved and customized to your viewing journey.</p>
            </div>
            <div class="feature-card">
                <img src="@/assets/feature3.svg" alt="Feature 3 icon" class="feature-icon">
                <h3>Enjoy AFL on Your Terms</h3>
                <p>Our app is built for ease and efficiency, focusing on delivering the information you want, precisely when you want it.</p>
            </div>
        </section>

        <section class="why-us">
            <div class="phone-mockup">
                <img src="@/assets/phone-mockup-angled.svg" alt="Later Ladder phone mockup">
            </div>
            <div class="why-us-content">
                <h2>Why Choose Later Ladder?</h2>
                <p>Later Ladder was created from a genuine passion for AFL and an understanding of the international fan's dilemma. We bridge the gap between your love for AFL and the practicality of your personal schedule. Our app ensures that you never have to compromise on the excitement of the game due to spoilers again.</p>
                <p>At Later Ladder, we're not just another app; we're your essential tool for enjoying AFL games on your schedule, keeping the spirit of competition alive and thrilling. Dive into your games, manage your standings, and enjoy AFL like never before, all through a straightforward, user-friendly platform.</p>
            </div>
        </section>

        <section class="cta">
            <div class="cta-text">
                <h2>Embrace each match at your pace, without spoilers.</h2>
            </div>
            <router-link to="/register" class="cta-button">Get Started</router-link>
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
    margin: 0 auto;
    background-color: var(--afl-800);
}

.hero {
    background: linear-gradient(var(--afl-800), var(--afl-600));
    padding: 4rem 0;
    margin-top: -2rem;
}

.hero-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 2rem 8rem 2rem 8rem;
}

.hero-text {
    flex: 1;
}

.hero-image {
    flex: 1;
    text-align: right;
}

.laptop-mockup {
    max-width: 100%;
}

h1 {
    font-size: 4rem;
    text-transform: uppercase;
    font-weight: 600;
    margin-bottom: 1rem;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.tagline {
    font-size: 1.75em;
    margin-bottom: 2rem;
}

.cta-button {
    display: inline-block;
    background-color: #7ec466;
    color: var(--afl-100);
    padding: 1rem 2rem;
    margin: 2rem 0 0 0;
    border-radius: 8px;
    text-decoration: none;
    font-size: 1.25rem;
    font-weight: bold;
    transition: background-color 0.3s ease;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.cta-button:hover {
    background-color: #95dd7e;
}

.mission {
    padding: 4rem 8rem;
    font-size: 1.25rem;
    text-align: center;
}

.mission-content {
    background: linear-gradient(var(--afl-800), var(--afl-600) 120%);
    padding: 1rem 4rem 3rem;
    margin: 0 8rem;
    border-radius: 6rem;
    border: 4px dashed #95dd7e;
}


.mission-text-box p {
    margin: 1rem 6rem;
}

h2 {
    font-size: 3rem;
    margin-bottom: 2rem;
    color: var(--afl-450);
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.features {
    display: flex;
    justify-content: space-between;
    margin: 1rem auto 4rem auto;
    max-width: 1200px;
    padding: 0 2rem;
}

.feature-card {
    flex: 1;
    text-align: center;
    padding: 2rem;
    margin: 2rem 1rem;
    background: linear-gradient(0deg, var(--afl-600) 30%, var(--afl-800) 100%);
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.feature-icon {
    padding: 2rem;
    width: 200px;
    height: 200px;
    margin-bottom: 0rem;
}

h3 {
    font-size: 1.75rem;
    margin-bottom: 1rem;
    color: var(--afl-400);
    text-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
}

.why-us {
    display: flex;
    justify-content: space-between;
    background-color: var(--afl-600);
    padding: 4rem 8rem;
    font-size: 1.25rem;
    text-align: center;
}

.phone-mockup {
    flex: 2;
    padding: 3rem 0 1rem;
    max-width: 50%;
}

.why-us-content {
    flex: 2;
    flex-basis: 50%;
    text-align: left;
    margin-left: 8rem;
}

.why-us-content h2 {
    color: #95dd7e;;
}

.cta {
    text-align: center;
    margin: 4rem 4rem -4rem 4rem;
    padding-bottom: 6rem;
}

.cta-text {
    background-color: var(--afl-600);
    padding: 1rem 4rem;
    margin: 0 10rem;
    border-radius: 8rem;
}

.cta h2 {
    color: var(--afl-200);
}

</style>