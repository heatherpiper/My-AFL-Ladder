<template>
    <div class="privacy-policy-container">

        <div class="header-box">
            <h1>Privacy Policy for Later Ladder</h1>
        </div>

        <div class="privacy-content">
            <p>This privacy policy will explain how Later Ladder uses the personal data we collect from you when you use our website as well as your privacy rights. By using the website, you agree to the collection and use of information in accordance with this privacy policy.</p>

            <h2>What data do we collect?</h2>
            <p>Later Ladder collects the following data:</p>
            <ul>
                <li><strong>Personal identification information</strong> - email address and username</li>
                <li><strong>Usage data</strong> - information on how you use the website, which may include your device's Internet Protocol addresss (IP address), browser type, browser version, the pages you visit, the time and date of your visit, the time spent on those pages, unique device identifiers, and other diagnostic data.</li>
            </ul>

            <h2>How will we use your data?</h2>
            <p>Later Ladder collects your data so that we can:</p>
            <ul>
                <li>Manage your account</li>
                <li>Email you with updates or important notices</li>
            </ul>
            <p>We may also use your information for other purposes, such as data analysis, identifying usage trends, or evaluating and improving website features.</p>

            <h2>Retention of your data</h2>
            <p>Later Ladder will keep your personal data for the duration required to fulfill the purposes stated in this privacy policy or as necessary to comply with legal obligations. Usage data may be retained to enhance website security and functionality.</p>

            <h2>What are your protection rights?</h2>
            <p>Later Ladder would like to make sure you are fully aware of all of your data protection rights. Every user is entitled to the following:</p>
            <ul>
                <li><strong>The right to access</strong> - You have the right to request Later Ladder for copies of your personal data.</li>
                <li><strong>The right to rectification</strong> - You have the right to request that Later Ladder correct any information you believe is inaccurate. You also have the right to request Later Ladder to complete the information you believe is incomplete.</li>
                <li><strong>The right to erasure</strong> - You have the right to request that Later Ladder erase your personal data, under certain conditions.</li>
                <li><strong>The right to restrict processing</strong> - You have the right to request that Later Ladder restrict the processing of your personal data, under certain conditions.</li>
                <li><strong>The right to object to processing</strong> - You have the right to object to Later Ladder's processing of your personal data, under certain conditions.</li>
                <li><strong>The right to data portability</strong> - You have the right to request that Later Ladder transfer the data that we have collected to another organization, or directly to you, under certain conditions.</li>
            </ul>
            <p>If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us through our email: <a href="mailto:support@laterladder.com">support@laterladder.com</a></p>

            <h2>Security of your data</h2>
            <p>The security of your pesonal data is important to us, but remember that no method of transmission over the internet or method of electronic storage is 100% secure. While we strive to use commercially acceptable means to protect your personal data, we cannot guarantee its absolute security.</p>

            <h2>Cookies</h2>
            <p>A cookie is a small file placed on your device. You can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some features of Later Ladder.</p>
            <p>Later Ladder uses cookies in the following ways:</p>

            <h3>Session cookies</h3>
            <p>Session cookies are deleted from your device as soon as you close your web browser. We use session cookies for the following purposes:</p>
            <ul>
                <li>To help authenticate users and prevent fraudulent use of user accounts.</li>
                <li>To enable unregistered guests to mark games as watched and update the team ladder on a temporary, non-persistent basis</li>
            </ul>

            <h3>Persistent cookies</h3>
            <p>Persistent cookies remain on your device when you go offline. We use persistent cookies for the following purpose:</p>
            <ul>
                <li>To remember choices you make when using the website, such as remembering your login details or whether to display final scores for watched games.</li>
            </ul>

            <p>At this time, we do not use cookies for the purpose of advertising in any way. We will not share any aspects of the data we collect with third parties for advertising purposes without first notifying our users.</p>
            <p>For further information about cookies and how they work, visit <a href="https://www.allaboutcookies.org">allaboutcookies.org</a>.</p>               

            <h2>Privacy policies of other websites</h2>
            <p>Later Ladder contains links to other websites. Our privacy policy applies only to our website, so if you click on a link to a third-party website, you should read their privacy policy to understand how your information will be used.</p>

            <h2>Changes to our privacy policy</h2>
            <p>Later Ladder may update our privacy policy from time to time. This privacy policy was last updated on March 31, 2024.</p>

            <h2>How to contact us</h2>
            <p>If you have any questions about Later Ladder's privacy policy, the data we hold on you, or you would like to exercise one of your data protection rights, please do not hesitate to contact us through our email: <a href="mailto:support@laterladder.com">support@laterladder.com</a></p>
    
        </div>

        <div class="button-container">
            <router-link class="back-button" to="/">Back to main page</router-link>
        </div>

    </div>
</template>

<script>
export default {
    name: 'privacy-policy',
}
</script>

<style scoped>

.privacy-policy-container {
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

.privacy-content {
    background-color: var(--afl-200);
    border-radius: 0 0 12px 12px;
    margin-bottom: 1.5em;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 1.75em;
    color: var(--afl-800);
    border: 1px solid var(--afl-800);
    line-height: 1.5em;
}

.privacy-content p:first-child {
    margin-top: 0.5em;
}

.privacy-content p {
    margin: 0.5em 0 0 0;
}

.privacy-content p:last-child {
    padding-bottom: 2em;
}

.privacy-content ul {
    padding-inline-start: 2.25em;
}

.privacy-content li {
    margin: 0.5em 0;
}

.privacy-content li strong {
    color: var(--afl-600);
}

.privacy-content h2 {
    color: var(--afl-500);
    font-size: x-large;
    margin-top: 1.5em;
    margin-bottom: 0;
}

.privacy-content h3 {
    color: var(--afl-600);
    margin-bottom: 0;
}

.privacy-content a {
    text-decoration: none;
    color: var(--afl-500);
}

.privacy-content a:hover {
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

    .privacy-policy-container {
        max-width: 100%;
    }

    .privacy-content {
        padding: 1.5em;
    }

    .privacy-content p {
        margin-top: 0.5em;
    }

    .privacy-content ul {
        padding-inline-start: 1.75em;
    }

    .privacy-content li {
        margin: 0.25em 0;
    }

    .privacy-content p:last-child {
        padding-bottom: 1em;
    }

    .back-button {
        display: block;
        max-width: 40%;
        background-color: var(--afl-450);
    }
}

@media (max-width: 600px) {

    .privacy-content {
        padding: 1em;
    }

    .privacy-content ul {
        padding-inline-start: 1.25em;
    }

    .section-content li {
        margin: 0.25em 0;
    }

    .back-button {
        max-width: 100%;
        padding: 0.75em 1.5em;
    }
}

</style>