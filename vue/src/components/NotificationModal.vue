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
    border-radius: 12px;
    box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.1), 0 4px 4px 0 rgba(0, 0, 0, 0.1);
    z-index: 1000;
  }

  .notification-modal p {
    color: var(--afl-800);
    text-align: center;
    margin-bottom: 1.25em;
  }

  .notification-modal button {
    display: block;
    margin: 0 auto;
    padding: 10px 32px;
    border: none;
    border-radius: 8px;
    background-color: var(--afl-500);
    color: var(--afl-200);
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.1s ease-in-out;
  }

  .notification-modal button:hover {
    background-color: var(--afl-450);
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
  