import { defineStore } from "pinia";
import { ref, watchEffect } from "vue";

export type ThemeMode = "light" | "dark";

const STORAGE_KEY = "echoplay-theme";

function getSystemPreference(): ThemeMode {
  if (typeof window === "undefined") return "light";
  return window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
}

function getStoredTheme(): ThemeMode | null {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored === "light" || stored === "dark") return stored;
  } catch {
    // localStorage not available
  }
  return null;
}

export const useThemeStore = defineStore("theme", () => {
  const stored = getStoredTheme();
  const mode = ref<ThemeMode>(stored ?? getSystemPreference());

  function applyTheme(m: ThemeMode) {
    document.documentElement.setAttribute("data-theme", m);
  }

  // Apply on change
  watchEffect(() => {
    applyTheme(mode.value);
    try {
      localStorage.setItem(STORAGE_KEY, mode.value);
    } catch {
      // ignore
    }
  });

  // Listen for system changes when no explicit preference is set
  if (typeof window !== "undefined") {
    window.matchMedia("(prefers-color-scheme: dark)").addEventListener("change", (e) => {
      if (getStoredTheme() === null) {
        mode.value = e.matches ? "dark" : "light";
      }
    });
  }

  function toggle() {
    mode.value = mode.value === "dark" ? "light" : "dark";
  }

  function setTheme(m: ThemeMode) {
    mode.value = m;
  }

  // Apply immediately on init
  applyTheme(mode.value);

  return { mode, toggle, setTheme };
});
