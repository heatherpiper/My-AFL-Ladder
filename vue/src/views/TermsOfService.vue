<template>
    <div class="terms-container">

        <div class="header-box">
            <h1>Website Terms and Conditions of Use</h1>
        </div>

        <div class="terms-content">
            <h2>1. Terms</h2>
            <p>When you access this Website, which is available at <a href="https://www.laterladder.com">https://www.laterladder.com</a>, you agree to comply with these Website Terms and Conditions of Use and acknowledge that you are responsible for adhering to any relevant local laws. If you do not agree with any of these terms, you are not allowed to access this site. The content on this Website is safeguarded by copyright and trademark law.</p>

            <h2>2. Use License</h2>
            <p>You are allowed to temporarily download a single copy of the content on Later Ladder's Website for personal, non-commercial, and short-term viewing purposes only. This permission is a license and does not transfer ownership to you. Under this license, you are prohibited from:</p>
            <ul>
                <li>altering or duplicating the content;</li>
                <li>using the content for commercial purposes or public display;</li>
                <li>attempting to reverse engineer any software on Later Ladder's Website;</li>
                <li>removing any copyright or proprietary notices from the content; or</li>
                <li>transferring the content to others or "mirroring" it on any other server.</li>
            </ul>
            <p>Later Ladder reserves the right to terminate this license if you violate any of these restrictions. Upon termination, your right to view the content will also be revoked, and you must delete any downloaded materials in your possession, whether in electronic or printed format.</p>

            <h2>3. Disclaimer</h2>
            <p>All materials on Later Ladder's Website are provided on an "as is" basis. Later Ladder does not make any warranties, whether express or implied, and therefore disclaims all other warranties. Moreover, Later Ladder does not make any representations about the accuracy or reliability of the use of the materials on its Website or any sites linked to this Website.</p>

            <h2>4. Limitations</h2>
            <p>Under no circumstances shall Later Ladder or its suppliers be held liable for any damages that arise from the use or inability to use the materials on Later Ladder's Website, even if Later Ladder or an authorized representative of this Website has been advised, either orally or in writing, of the possibility of such damage. Some jurisdictions do not allow limitations on implied warranties or limitations of liability for incidental damages, so these limitations may not apply to you.</p>

            <h2>5. Revisions and Errata</h2>
            <p>The materials appearing on Later Ladder's Website may contain technical, typographical, or photographic errors. Later Ladder does not warrant that any of the materials on its Website are accurate, complete, or up-to-date. Later Ladder may modify the materials on its Website at any time without prior notice. However, Later Ladder does not commit to updating the materials.</p>

            <h2>6. Links</h2>
            <p>Later Ladder is not responsible for the content of any linked site. The inclusion of any link does not imply endorsement by Later Ladder of the site. Use of any linked website is at the user's own risk.</p>

            <h2>7. Site Terms of Use Modifications</h2>
            <p>Later Ladder may revise these Terms of Use for its Website at any time without prior notice. By using this Website, you agree to be bound by the then-current version of these Terms and Conditions of Use.</p>

            <h2>8. Your Privacy</h2>
            <p>Please review our <a href="https://www.laterladder.com/privacy-policy">Privacy Policy</a>.</p>

            <h2>9. Governing Law</h2>
            <p>Any claim relating to Later Ladder's Website shall be governed by the laws of the United States, without regard to its conflict of law provisions.</p>
        </div>

        <div class="button-container">
            <router-link class="back-button" to="/">Back to main page</router-link>
        </div>

    </div>
</template>

<script>
export default {
    name: 'TermsOfService',
}
</script>

<style scoped>

.terms-container {
    max-width: 80%;
    margin: auto;
}

.header-box {
    background-color: var(--afl-800);
    border-radius: 12px 12px 0 0;
    box-shadow: 8px 8px 8px rgba(0, 0, 0, 0.1);
    padding: 4px 12px;
}

.header-box h1 {
    font-family: 'Roboto', sans-serif;
    font-size: xx-large;
    color: var(--afl-200);
    margin: 0;
    text-shadow: 1px 1px 2px var(--afl-900);
}

.terms-content {
    background-color: var(--afl-200);
    border-radius: 0 0 12px 12px;
    margin-bottom: 1.5em;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 10px 24px 24px 24px;
    color: var(--afl-800);
    border: 1px solid var(--afl-800);
    line-height: 1.5em;
}

.terms-content p {
    margin-top: 0.75em;
}

.terms-content li {
    margin: 0 1em;
}

.terms-content h2 {
    color: var(--afl-500);
    font-size: x-large;
    margin-top: 1.5em;
    margin-bottom: 0;
}

.terms-content a {
    text-decoration: none;
    color: var(--afl-500);
}

.terms-content a:hover {
    text-decoration: underline;
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
    .terms-container {
        max-width: 100%;
    }

    .terms-content {
        padding: 10px 16px 16px 16px;
    }

    ul {
        padding-left: 0;
    }

    .back-button {
        display: block;
        background-color: var(--afl-450);
    }
}

</style>