// ==========================================
// Configuration
// ==========================================
const CONFIG = {
  GITHUB_API: "https://api.github.com",
  REPO_OWNER: "Mosberg",
  REPO_NAME: "mosbergapi",
  BRANCH: "main",
  FILES_TO_LOAD: [
    "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/README.md",
    "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CONTRIBUTING.md",
    "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CHANGELOG.md",
  ],
};

// ==========================================
// State Management
// ==========================================
const state = {
  theme: localStorage.getItem("theme") || "light",
  content: {},
  repoData: null,
  currentSection: "overview",
};

// ==========================================
// DOM Elements
// ==========================================
const elements = {
  loadingOverlay: document.getElementById("loadingOverlay"),
  themeToggle: document.getElementById("themeToggle"),
  header: document.getElementById("header"),
  content: document.getElementById("content"),
  sidebarNav: document.getElementById("sidebarNav"),
  tocNav: document.getElementById("tocNav"),
  searchInput: document.getElementById("searchInput"),
  scrollToTop: document.getElementById("scrollToTop"),
  mobileMenuToggle: document.getElementById("mobileMenuToggle"),
  navMenu: document.getElementById("navMenu"),
  lastUpdated: document.getElementById("lastUpdated"),
  statsStars: document.getElementById("statsStars"),
  statsCommits: document.getElementById("statsCommits"),
};

// ==========================================
// Initialization
// ==========================================
document.addEventListener("DOMContentLoaded", async () => {
  console.log("🚀 MosbergAPI Documentation Loading...");

  // Initialize theme
  initTheme();

  // Load content
  await loadAllContent();

  // Initialize navigation
  initNavigation();

  // Initialize scroll handlers
  initScrollHandlers();

  // Initialize search
  initSearch();

  // Load GitHub stats
  await loadGitHubStats();

  // Hide loading overlay
  setTimeout(() => {
    elements.loadingOverlay.classList.add("hidden");
  }, 500);

  console.log("✅ Documentation loaded successfully!");
});

// ==========================================
// Theme Management
// ==========================================
function initTheme() {
  document.documentElement.setAttribute("data-theme", state.theme);

  elements.themeToggle.addEventListener("click", () => {
    state.theme = state.theme === "light" ? "dark" : "light";
    document.documentElement.setAttribute("data-theme", state.theme);
    localStorage.setItem("theme", state.theme);
  });
}

// ==========================================
// Content Loading
// ==========================================
async function loadAllContent() {
  try {
    // Load README.md
    const readmeContent = await fetchGitHubFile("README.md");
    state.content.readme = parseMarkdownSections(readmeContent);

    // Load CONTRIBUTING.md
    const contributingContent = await fetchGitHubFile("CONTRIBUTING.md");
    state.content.contributing = contributingContent;

    // Load CHANGELOG.md
    const changelogContent = await fetchGitHubFile("CHANGELOG.md");
    state.content.changelog = changelogContent;

    // Render content
    renderContent();
  } catch (error) {
    console.error("Error loading content:", error);
    showError("Failed to load documentation. Please try again later.");
  }
}

async function fetchGitHubFile(filename) {
  const url = `https://raw.githubusercontent.com/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}/${CONFIG.BRANCH}/${filename}`;

  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`Failed to fetch ${filename}: ${response.statusText}`);
    }
    return await response.text();
  } catch (error) {
    console.error(`Error fetching ${filename}:`, error);
    return `# Error\n\nFailed to load ${filename}. Please check the [GitHub repository](https://github.com/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}).`;
  }
}

function parseMarkdownSections(markdown) {
  const sections = {};
  const lines = markdown.split("\n");
  let currentSection = null;
  let currentContent = [];

  for (const line of lines) {
    if (line.startsWith("## ")) {
      if (currentSection) {
        sections[currentSection] = currentContent.join("\n");
      }
      currentSection = line.substring(3).trim();
      currentContent = [];
    } else if (currentSection) {
      currentContent.push(line);
    }
  }

  if (currentSection) {
    sections[currentSection] = currentContent.join("\n");
  }

  return sections;
}

function renderContent() {
  // Map sections to content
  const sectionMapping = {
    overview:
      state.content.readme["📖 Overview"] ||
      state.content.readme["Overview"] ||
      "",
    "getting-started":
      state.content.readme["🚀 Getting Started"] ||
      state.content.readme["Getting Started"] ||
      "",
    features:
      state.content.readme["✨ Core Features"] ||
      state.content.readme["Features"] ||
      "",
    "api-reference":
      state.content.readme["📚 Usage Examples"] ||
      state.content.readme["API Reference"] ||
      "",
    examples:
      state.content.readme["📚 Usage Examples"] ||
      state.content.readme["Examples"] ||
      "",
    contributing: state.content.contributing || "",
    changelog: state.content.changelog || "",
  };

  // Render each section
  Object.entries(sectionMapping).forEach(([sectionId, content]) => {
    const section = document.getElementById(sectionId);
    if (section && content) {
      const htmlContent = marked.parse(content);
      section.innerHTML = `<h2>${
        section.querySelector("h2").textContent
      }</h2>${htmlContent}`;

      // Highlight code blocks
      section.querySelectorAll("pre code").forEach((block) => {
        hljs.highlightElement(block);
      });
    }
  });

  // Generate table of contents
  generateTableOfContents();
  generateSidebarNav();
}

function generateTableOfContents() {
  const content = elements.content;
  const headings = content.querySelectorAll("h2, h3");
  const tocItems = [];

  headings.forEach((heading) => {
    const id =
      heading.id ||
      heading.textContent.toLowerCase().replace(/[^a-z0-9]+/g, "-");
    heading.id = id;

    const level = parseInt(heading.tagName.substring(1));
    const text = heading.textContent;

    tocItems.push(`
            <a href="#${id}" class="toc-link level-${level}" data-target="${id}">
                ${text}
            </a>
        `);
  });

  elements.tocNav.innerHTML = tocItems.join("");

  // Add click handlers
  elements.tocNav.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const target = document.getElementById(link.dataset.target);
      if (target) {
        target.scrollIntoView({ behavior: "smooth" });
      }
    });
  });
}

function generateSidebarNav() {
  const navItems = [
    { id: "overview", icon: "📖", text: "Overview" },
    { id: "getting-started", icon: "🚀", text: "Getting Started" },
    { id: "features", icon: "✨", text: "Features" },
    { id: "api-reference", icon: "📚", text: "API Reference" },
    { id: "examples", icon: "💡", text: "Examples" },
    { id: "contributing", icon: "🤝", text: "Contributing" },
    { id: "changelog", icon: "📜", text: "Changelog" },
  ];

  const navHtml = navItems
    .map(
      (item) => `
        <a href="#${item.id}" data-section="${item.id}">
            <span class="nav-icon">${item.icon}</span>
            ${item.text}
        </a>
    `
    )
    .join("");

  elements.sidebarNav.innerHTML = navHtml;
}

// ==========================================
// Navigation
// ==========================================
function initNavigation() {
  // Header navigation
  document.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const href = link.getAttribute("href");
      const target = document.querySelector(href);
      if (target) {
        target.scrollIntoView({ behavior: "smooth" });
      }
    });
  });

  // Sidebar navigation
  document.querySelectorAll(".sidebar-nav a, .nav-menu a").forEach((link) => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const href = link.getAttribute("href");
      const target = document.querySelector(href);
      if (target) {
        target.scrollIntoView({ behavior: "smooth" });
        updateActiveNavigation(href.substring(1));
      }
    });
  });

  // Mobile menu toggle
  elements.mobileMenuToggle.addEventListener("click", () => {
    elements.navMenu.classList.toggle("active");
  });
}

function updateActiveNavigation(sectionId) {
  // Update header navigation
  document.querySelectorAll(".nav-link").forEach((link) => {
    const href = link.getAttribute("href").substring(1);
    link.classList.toggle("active", href === sectionId);
  });

  // Update sidebar navigation
  document.querySelectorAll(".sidebar-nav a").forEach((link) => {
    const section = link.dataset.section;
    link.classList.toggle("active", section === sectionId);
  });

  // Update TOC
  document.querySelectorAll(".toc-nav a").forEach((link) => {
    const target = link.dataset.target;
    link.classList.toggle("active", target === sectionId);
  });
}

// ==========================================
// Scroll Handlers
// ==========================================
function initScrollHandlers() {
  let lastScrollTop = 0;

  window.addEventListener("scroll", () => {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

    // Header scroll effect
    if (scrollTop > 50) {
      elements.header.classList.add("scrolled");
    } else {
      elements.header.classList.remove("scrolled");
    }

    // Scroll to top button
    if (scrollTop > 300) {
      elements.scrollToTop.classList.add("visible");
    } else {
      elements.scrollToTop.classList.remove("visible");
    }

    // Update active section
    updateActiveSection();

    lastScrollTop = scrollTop;
  });

  // Scroll to top button click
  elements.scrollToTop.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}

function updateActiveSection() {
  const sections = document.querySelectorAll(".content-section");
  const scrollPos = window.pageYOffset + 200;

  sections.forEach((section) => {
    const top = section.offsetTop;
    const bottom = top + section.offsetHeight;
    const id = section.getAttribute("id");

    if (scrollPos >= top && scrollPos < bottom) {
      updateActiveNavigation(id);
    }
  });
}

// ==========================================
// Search Functionality
// ==========================================
function initSearch() {
  let searchTimeout;

  elements.searchInput.addEventListener("input", (e) => {
    clearTimeout(searchTimeout);
    const query = e.target.value.toLowerCase().trim();

    if (query.length < 2) {
      clearSearch();
      return;
    }

    searchTimeout = setTimeout(() => {
      performSearch(query);
    }, 300);
  });
}

function performSearch(query) {
  const sections = document.querySelectorAll(".content-section");
  let foundResults = false;

  sections.forEach((section) => {
    const text = section.textContent.toLowerCase();
    const matches = text.includes(query);

    if (matches) {
      section.style.display = "block";
      highlightSearchTerm(section, query);
      foundResults = true;
    } else {
      section.style.display = "none";
    }
  });

  if (!foundResults) {
    showNoResults();
  }
}

function highlightSearchTerm(element, term) {
  // Simple highlight implementation
  const regex = new RegExp(`(${term})`, "gi");
  const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT);
  const nodes = [];

  while (walker.nextNode()) {
    nodes.push(walker.currentNode);
  }

  nodes.forEach((node) => {
    if (
      node.parentElement.tagName !== "SCRIPT" &&
      node.parentElement.tagName !== "STYLE"
    ) {
      const text = node.textContent;
      if (regex.test(text)) {
        const span = document.createElement("span");
        span.innerHTML = text.replace(regex, "<mark>$1</mark>");
        node.parentElement.replaceChild(span, node);
      }
    }
  });
}

function clearSearch() {
  const sections = document.querySelectorAll(".content-section");
  sections.forEach((section) => {
    section.style.display = "block";
  });

  // Remove highlights
  document.querySelectorAll("mark").forEach((mark) => {
    const text = document.createTextNode(mark.textContent);
    mark.parentNode.replaceChild(text, mark);
  });
}

function showNoResults() {
  // Implement no results message
  console.log("No search results found");
}

// ==========================================
// GitHub Stats
// ==========================================
async function loadGitHubStats() {
  try {
    // Fetch repository data
    const repoResponse = await fetch(
      `${CONFIG.GITHUB_API}/repos/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}`
    );
    const repoData = await repoResponse.json();

    // Fetch commit count
    const commitsResponse = await fetch(
      `${CONFIG.GITHUB_API}/repos/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}/commits?per_page=1`
    );
    const linkHeader = commitsResponse.headers.get("Link");
    let commitCount = 1;

    if (linkHeader) {
      const match = linkHeader.match(/page=(\d+)>; rel="last"/);
      if (match) {
        commitCount = parseInt(match[1]);
      }
    }

    // Update UI
    if (elements.statsStars) {
      elements.statsStars.textContent = repoData.stargazers_count || "0";
    }

    if (elements.statsCommits) {
      elements.statsCommits.textContent = commitCount;
    }

    // Update last updated date
    if (elements.lastUpdated) {
      const lastUpdate = new Date(repoData.updated_at);
      elements.lastUpdated.textContent = lastUpdate.toLocaleDateString(
        "en-US",
        {
          year: "numeric",
          month: "long",
          day: "numeric",
        }
      );
    }

    state.repoData = repoData;
  } catch (error) {
    console.error("Error loading GitHub stats:", error);
  }
}

// ==========================================
// Error Handling
// ==========================================
function showError(message) {
  const errorHtml = `
        <div class="error-message" style="padding: 2rem; text-align: center; color: var(--color-danger);">
            <h2>⚠️ Error</h2>
            <p>${message}</p>
            <a href="https://github.com/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}" class="btn btn-primary" target="_blank">
                Visit GitHub Repository
            </a>
        </div>
    `;
  elements.content.innerHTML = errorHtml;
}

// ==========================================
// Auto-reload on changes (for development)
// ==========================================
if (
  window.location.hostname === "localhost" ||
  window.location.hostname === "127.0.0.1"
) {
  setInterval(async () => {
    console.log("🔄 Checking for updates...");
    await loadAllContent();
  }, 60000); // Check every minute
}

// Export for debugging
window.MosbergAPIDocs = {
  state,
  CONFIG,
  reload: loadAllContent,
};
