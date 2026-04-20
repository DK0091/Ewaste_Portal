// Theme toggle — persists preference in localStorage
(function () {
    const saved = localStorage.getItem('ecoTheme') || 'light';
    document.documentElement.setAttribute('data-theme', saved);
})();

function toggleTheme() {
    const current = document.documentElement.getAttribute('data-theme');
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('ecoTheme', next);
    updateThemeIcon(next);
}

function updateThemeIcon(theme) {
    const btn = document.getElementById('themeToggleBtn');
    if (!btn) return;
    if (theme === 'dark') {
        btn.innerHTML = '<i class="fa-solid fa-sun"></i>';
        btn.setAttribute('title', 'Switch to Light Mode');
    } else {
        btn.innerHTML = '<i class="fa-solid fa-moon"></i>';
        btn.setAttribute('title', 'Switch to Dark Mode');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const current = document.documentElement.getAttribute('data-theme') || 'light';
    updateThemeIcon(current);
});
