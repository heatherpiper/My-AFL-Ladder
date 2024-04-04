<template>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <router-link to="/privacy-policy" class="footer-link">Privacy Policy</router-link>
          <router-link to="/terms-of-service" class="footer-link">Terms of Service</router-link>
          <a href="mailto:support@laterladder.com" class="footer-link">Contact Us</a>
        </div>
        <div class="copyright">
          &copy; 2024 Later Ladder. All rights reserved.
        </div>
      </div>
    </footer>
</template>

<script>
export default {
    name: 'Footer',
    components: {},
}

</script>

<style scoped>
.footer {
  background-color: var(--afl-800);
  color: var(--afl-250);
  padding: 1.5em 0;
  text-align: center;
  margin-top: auto;
}

.footer-content {
  font-size: 0.9em;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.footer-links {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 0.5em;
  line-height: 1.25;
}

.footer-link {
  color: var(--afl-250);
  text-decoration: none;
  margin: 0 0.75em;
}

.footer-link:hover {
  text-decoration: underline;
}

.footer-link:visited {
  color: var(--afl-250);
}

.copyright {
  color: var(--afl-250);
  margin-top: 0.5em;
}

@media (max-width: 600px) {
  .footer-links {
    flex-direction: column;
    align-items: center;
  }
}
</style>