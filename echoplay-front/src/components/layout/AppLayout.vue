<script setup lang="ts">
import AppHeader from "./AppHeader.vue";
import AppSidebar from "./AppSidebar.vue";

const emit = defineEmits<{
  search: [keyword: string];
}>();
</script>

<template>
  <div class="app-layout">
    <AppHeader @search="emit('search', $event)" />
    <div class="layout-body">
      <AppSidebar />
      <main class="layout-main">
        <slot />
      </main>
    </div>
  </div>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: var(--color-bg-page);
  transition: background-color var(--duration-normal) var(--ease-default);
}

.layout-body {
  display: flex;
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding-top: var(--header-height);
}

.layout-main {
  flex: 1;
  min-width: 0;
  padding: var(--space-6);
}

@media (max-width: 1024px) {
  .layout-main {
    padding: var(--space-4);
  }
}
</style>
