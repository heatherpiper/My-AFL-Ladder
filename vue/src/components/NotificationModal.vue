<template>
    <transition name="fade">
        <div class="notification-modal">
        <p>{{ message }}</p>
        <button @click="$emit('close')">OK</button>
        </div>
    </transition>
  </template>
  
  <script>
  export default {
    props: ['message'],
  }
  </script>
  
  <style>
  .notification-modal {
    position: fixed;
    top: 18%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: var(--afl-200);
    padding: 16px 12px;
    margin-top: 1em;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0,0,0,.5);
    z-index: 1000;
  }

  .notification-modal p {
    text-align: center;
    margin-bottom: 1.25em;
  }

  .notification-modal button {
    display: block;
    margin: 0 auto;
    padding: 8px 32px;
    border: none;
    border-radius: 8px;
    background-color: var(--afl-600);
    color: var(--afl-200);
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.5s;
  }

  .fade-enter, .fade-leave-to {
    opacity: 0;
  }

  @media (max-width: 768px) {
    .notification-modal {
        padding: 16px;
        min-width: 280px;
    }
  }

  </style>
  