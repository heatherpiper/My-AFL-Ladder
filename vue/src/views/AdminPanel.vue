<template>
    <div class="admin-panel">
        <div class="sidebar">
            <ul>
                <li><router-link to="/admin/games">Games Management</router-link></li>
                <li><router-link to="/admin/users">User Management</router-link></li>
            </ul>
        </div>
        <div class="content">
            <router-view/>
        </div>
    </div>
</template>

<style scoped>

</style>