// ==========================================
// Configuration
// ==========================================
const CONFIG = {
  GITHUB_API: "https://api.github.com",
  REPO_OWNER: "Mosberg",
  REPO_NAME: "mosbergapi",
  BRANCH: "main",
  RAW_BASE_URL: "https://raw.githubusercontent.com/Mosberg/mosbergapi/main",
  FILES_TO_LOAD: {
    readme:
      "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/README.md",
    contributing:
      "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CONTRIBUTING.md",
    changelog:
      "https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CHANGELOG.md",
  },
};

// ==========================================
// State Management
// ==========================================
const state = {
  theme: localStorage.getItem("theme") || "light",
  content: {
    readme: {},
    contributing: "",
    changelog: "",
  },
  repoData: null,
  currentSection: "overview",
  lastFetch: null,
  cacheExpiry: 5 * 60 * 1000, // 5 minutes
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
  console.log("📦 Config:", CONFIG);

  // Initialize theme
  initTheme();

  // Load content with retry mechanism
  let retries = 3;
  while (retries > 0) {
    try {
      await loadAllContent();
      break;
    } catch (error) {
      console.error(`❌ Load attempt failed (${retries} retries left):`, error);
      retries--;
      if (retries === 0) {
        showError(
          "Failed to load documentation after multiple attempts. Please refresh the page or visit the GitHub repository."
        );
      }
      await new Promise((resolve) => setTimeout(resolve, 1000));
    }
  }

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
  // Check for system preference if no saved theme
  if (!localStorage.getItem("theme")) {
    const prefersDark = window.matchMedia(
      "(prefers-color-scheme: dark)"
    ).matches;
    state.theme = prefersDark ? "dark" : "light";
  }

  document.documentElement.setAttribute("data-theme", state.theme);

  elements.themeToggle.addEventListener("click", () => {
    state.theme = state.theme === "light" ? "dark" : "light";
    document.documentElement.setAttribute("data-theme", state.theme);
    localStorage.setItem("theme", state.theme);
    console.log("🎨 Theme changed to:", state.theme);
  });
}

// ==========================================
// Content Loading
// ==========================================
async function loadAllContent() {
  console.log("📥 Loading content from GitHub...");

  try {
    // Load all files in parallel
    const [readmeContent, contributingContent, changelogContent] =
      await Promise.all([
        fetchContent(CONFIG.FILES_TO_LOAD.readme, "README.md"),
        fetchContent(CONFIG.FILES_TO_LOAD.contributing, "CONTRIBUTING.md"),
        fetchContent(CONFIG.FILES_TO_LOAD.changelog, "CHANGELOG.md"),
      ]);

    // Parse README into sections
    state.content.readme = parseMarkdownSections(readmeContent);
    state.content.contributing = contributingContent;
    state.content.changelog = changelogContent;

    state.lastFetch = Date.now();

    console.log("✅ Content loaded successfully");
    console.log("📄 README sections:", Object.keys(state.content.readme));

    // Render content
    renderContent();
  } catch (error) {
    console.error("❌ Error loading content:", error);
    throw error;
  }
}

async function fetchContent(url, filename) {
  console.log(`📡 Fetching ${filename} from ${url}`);

  try {
    const response = await fetch(url, {
      cache: "no-cache",
      headers: {
        Accept: "text/plain",
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    const content = await response.text();
    console.log(`✅ ${filename} loaded (${content.length} characters)`);
    return content;
  } catch (error) {
    console.error(`❌ Failed to fetch ${filename}:`, error);
    // Return fallback content
    return `# ${filename.replace(
      ".md",
      ""
    )}\n\nContent temporarily unavailable. Please visit the [GitHub repository](https://github.com/${
      CONFIG.REPO_OWNER
    }/${CONFIG.REPO_NAME}) to view this file.\n\n**Error:** ${error.message}`;
  }
}

function parseMarkdownSections(markdown) {
  const sections = {};
  const lines = markdown.split("\n");
  let currentSection = null;
  let currentContent = [];

  // Skip the main title (first # heading)
  let skipFirstTitle = true;

  for (const line of lines) {
    // Detect level 2 headings (##)
    if (line.match(/^## /)) {
      // Save previous section
      if (currentSection) {
        sections[currentSection] = currentContent.join("\n").trim();
      }

      // Start new section
      currentSection = line.substring(3).trim();
      // Remove emoji and clean up section name
      currentSection = currentSection
        .replace(/[📖🚀✨📚💡🤝📜🏗️📦🔨🧪📄🐛📞📊🙏]/g, "")
        .trim();
      currentContent = [];
    } else if (line.match(/^# /) && skipFirstTitle) {
      // Skip the main title
      skipFirstTitle = false;
      continue;
    } else if (currentSection) {
      currentContent.push(line);
    }
  }

  // Save last section
  if (currentSection) {
    sections[currentSection] = currentContent.join("\n").trim();
  }

  return sections;
}

function renderContent() {
  console.log("🎨 Rendering content...");

  // Create section mapping with fallbacks
  const sectionMapping = {
    overview: getSectionContent(["Overview", "About", "Introduction"]),
    "getting-started": getSectionContent([
      "Getting Started",
      "Installation",
      "Setup",
    ]),
    features: getSectionContent(["Core Features", "Features", "Highlights"]),
    "api-reference": getSectionContent([
      "API Reference",
      "Documentation",
      "Usage",
    ]),
    examples: getSectionContent(["Usage Examples", "Examples", "Tutorials"]),
    contributing: state.content.contributing || "",
    changelog: state.content.changelog || "",
  };

  // Render each section
  Object.entries(sectionMapping).forEach(([sectionId, content]) => {
    const section = document.getElementById(sectionId);
    if (section) {
      const heading = section.querySelector("h2");
      const headingText = heading
        ? heading.textContent
        : sectionId.replace("-", " ").replace(/\b\w/g, (l) => l.toUpperCase());

      if (content) {
        try {
          const htmlContent = marked.parse(content);
          section.innerHTML = `<h2 id="${sectionId}-heading">${headingText}</h2>${htmlContent}`;

          // Highlight code blocks
          section.querySelectorAll("pre code").forEach((block) => {
            // Detect language from class or content
            const lang = detectLanguage(block);
            if (lang) {
              block.className = `language-${lang}`;
            }
            hljs.highlightElement(block);
          });

          // Make external links open in new tab
          section.querySelectorAll('a[href^="http"]').forEach((link) => {
            link.setAttribute("target", "_blank");
            link.setAttribute("rel", "noopener noreferrer");
          });

          console.log(`✅ Rendered section: ${sectionId}`);
        } catch (error) {
          console.error(`❌ Error rendering section ${sectionId}:`, error);
          section.innerHTML = `<h2>${headingText}</h2><p class="error">Failed to render content.</p>`;
        }
      } else {
        section.innerHTML = `<h2>${headingText}</h2><p class="text-muted">Content not available.</p>`;
      }
    }
  });

  // Generate navigation
  generateTableOfContents();
  generateSidebarNav();

  console.log("✅ Content rendered successfully");
}

function getSectionContent(possibleNames) {
  for (const name of possibleNames) {
    if (state.content.readme[name]) {
      return state.content.readme[name];
    }
  }
  return "";
}

function detectLanguage(codeBlock) {
  const code = codeBlock.textContent;

  // Check for Java
  if (code.includes("public class") || code.includes("import net.minecraft")) {
    return "java";
  }

  // Check for Gradle
  if (code.includes("dependencies {") || code.includes("repositories {")) {
    return "gradle";
  }

  // Check for JSON
  if (code.trim().startsWith("{") && code.includes('":')) {
    return "json";
  }

  // Check for Bash/Shell
  if (
    code.includes("./gradlew") ||
    code.includes("git ") ||
    code.includes("mkdir")
  ) {
    return "bash";
  }

  return null;
}

function generateTableOfContents() {
  const content = elements.content;
  const headings = content.querySelectorAll("h2, h3, h4");
  const tocItems = [];

  headings.forEach((heading, index) => {
    let id = heading.id;
    if (!id) {
      id =
        "heading-" +
        index +
        "-" +
        heading.textContent.toLowerCase().replace(/[^a-z0-9]+/g, "-");
      heading.id = id;
    }

    const level = parseInt(heading.tagName.substring(1));
    const text = heading.textContent;
    const indent = (level - 2) * 12; // Indent based on level

    tocItems.push(`
            <a href="#${id}" class="toc-link level-${level}" data-target="${id}" style="padding-left: ${indent}px;">
                ${text}
            </a>
        `);
  });

  if (elements.tocNav) {
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

  if (elements.sidebarNav) {
    elements.sidebarNav.innerHTML = navHtml;
  }
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
  if (elements.mobileMenuToggle) {
    elements.mobileMenuToggle.addEventListener("click", () => {
      elements.navMenu.classList.toggle("active");
    });
  }
}

function updateActiveNavigation(sectionId) {
  // Update header navigation
  document.querySelectorAll(".nav-link").forEach((link) => {
    const href = link.getAttribute("href");
    if (href) {
      link.classList.toggle("active", href.substring(1) === sectionId);
    }
  });

  // Update sidebar navigation
  document.querySelectorAll(".sidebar-nav a").forEach((link) => {
    const section = link.dataset.section;
    link.classList.toggle("active", section === sectionId);
  });

  // Update TOC
  document.querySelectorAll(".toc-nav a").forEach((link) => {
    const target = link.dataset.target;
    if (target) {
      link.classList.toggle("active", target.startsWith(sectionId));
    }
  });
}

// ==========================================
// Scroll Handlers
// ==========================================
function initScrollHandlers() {
  let lastScrollTop = 0;
  let ticking = false;

  window.addEventListener("scroll", () => {
    if (!ticking) {
      window.requestAnimationFrame(() => {
        handleScroll();
        ticking = false;
      });
      ticking = true;
    }
  });

  // Scroll to top button click
  if (elements.scrollToTop) {
    elements.scrollToTop.addEventListener("click", () => {
      window.scrollTo({ top: 0, behavior: "smooth" });
    });
  }
}

function handleScroll() {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

  // Header scroll effect
  if (elements.header) {
    if (scrollTop > 50) {
      elements.header.classList.add("scrolled");
    } else {
      elements.header.classList.remove("scrolled");
    }
  }

  // Scroll to top button
  if (elements.scrollToTop) {
    if (scrollTop > 300) {
      elements.scrollToTop.classList.add("visible");
    } else {
      elements.scrollToTop.classList.remove("visible");
    }
  }

  // Update active section
  updateActiveSection();
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
  if (!elements.searchInput) return;

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

  // Clear search on Escape
  elements.searchInput.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      elements.searchInput.value = "";
      clearSearch();
    }
  });
}

function performSearch(query) {
  const sections = document.querySelectorAll(".content-section");
  let foundResults = 0;

  sections.forEach((section) => {
    const text = section.textContent.toLowerCase();
    const matches = text.includes(query);

    if (matches) {
      section.style.display = "block";
      highlightSearchTerm(section, query);
      foundResults++;
    } else {
      section.style.display = "none";
    }
  });

  console.log(
    `🔍 Search results for "${query}": ${foundResults} sections found`
  );
}

function highlightSearchTerm(element, term) {
  // Remove existing highlights
  element.querySelectorAll("mark.search-highlight").forEach((mark) => {
    const text = document.createTextNode(mark.textContent);
    mark.parentNode.replaceChild(text, mark);
  });

  // Simple text highlighting (avoiding code blocks and headings)
  const regex = new RegExp(`(${escapeRegex(term)})`, "gi");
  const textNodes = getTextNodes(element);

  textNodes.forEach((node) => {
    const parent = node.parentElement;
    if (parent && !parent.closest("pre, code, h1, h2, h3, h4, h5, h6")) {
      const text = node.textContent;
      if (regex.test(text)) {
        const span = document.createElement("span");
        span.innerHTML = text.replace(
          regex,
          '<mark class="search-highlight">$1</mark>'
        );
        parent.replaceChild(span, node);
      }
    }
  });
}

function getTextNodes(element) {
  const textNodes = [];
  const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT, {
    acceptNode: (node) => {
      return node.textContent.trim()
        ? NodeFilter.FILTER_ACCEPT
        : NodeFilter.FILTER_REJECT;
    },
  });

  let node;
  while ((node = walker.nextNode())) {
    textNodes.push(node);
  }

  return textNodes;
}

function escapeRegex(string) {
  return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}

function clearSearch() {
  const sections = document.querySelectorAll(".content-section");
  sections.forEach((section) => {
    section.style.display = "block";
  });

  // Remove all search highlights
  document.querySelectorAll("mark.search-highlight").forEach((mark) => {
    const text = document.createTextNode(mark.textContent);
    mark.parentNode.replaceChild(text, mark);
  });
}

// ==========================================
// GitHub Stats
// ==========================================
async function loadGitHubStats() {
  console.log("📊 Loading GitHub stats...");

  try {
    // Fetch repository data
    const repoUrl = `${CONFIG.GITHUB_API}/repos/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}`;
    console.log("📡 Fetching repo data from:", repoUrl);

    const repoResponse = await fetch(repoUrl);
    if (!repoResponse.ok) {
      throw new Error(`GitHub API error: ${repoResponse.status}`);
    }

    const repoData = await repoResponse.json();
    console.log("✅ Repository data loaded:", repoData);

    // Fetch commit count
    const commitsUrl = `${CONFIG.GITHUB_API}/repos/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}/commits?per_page=1`;
    const commitsResponse = await fetch(commitsUrl);

    let commitCount = 0;
    if (commitsResponse.ok) {
      const linkHeader = commitsResponse.headers.get("Link");
      if (linkHeader) {
        const match = linkHeader.match(/page=(\d+)>; rel="last"/);
        if (match) {
          commitCount = parseInt(match[1]);
        }
      } else {
        // If no link header, there's at least 1 commit
        commitCount = 1;
      }
    }

    // Update UI
    if (elements.statsStars) {
      elements.statsStars.textContent = repoData.stargazers_count || "0";
    }

    if (elements.statsCommits) {
      elements.statsCommits.textContent = commitCount || "-";
    }

    // Update last updated date
    if (elements.lastUpdated && repoData.updated_at) {
      const lastUpdate = new Date(repoData.updated_at);
      elements.lastUpdated.textContent = lastUpdate.toLocaleDateString(
        "en-US",
        {
          year: "numeric",
          month: "long",
          day: "numeric",
          hour: "2-digit",
          minute: "2-digit",
        }
      );
    }

    state.repoData = repoData;
    console.log("✅ GitHub stats loaded successfully");
  } catch (error) {
    console.error("❌ Error loading GitHub stats:", error);
    // Set fallback values
    if (elements.statsStars) elements.statsStars.textContent = "-";
    if (elements.statsCommits) elements.statsCommits.textContent = "-";
    if (elements.lastUpdated) elements.lastUpdated.textContent = "Recently";
  }
}

// ==========================================
// Error Handling
// ==========================================
function showError(message) {
  const errorHtml = `
        <div class="error-message" style="padding: 3rem; text-align: center; background: var(--color-surface); border-radius: var(--radius-lg); margin: 2rem 0;">
            <h2 style="color: var(--color-danger); margin-bottom: 1rem;">⚠️ Error Loading Documentation</h2>
            <p style="color: var(--color-text-secondary); margin-bottom: 2rem;">${message}</p>
            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                <a href="https://github.com/${CONFIG.REPO_OWNER}/${CONFIG.REPO_NAME}" class="btn btn-primary" target="_blank" rel="noopener">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor" style="margin-right: 0.5rem;">
                        <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>
                    </svg>
                    Visit GitHub Repository
                </a>
                <button onclick="window.location.reload()" class="btn btn-secondary">
                    🔄 Reload Page
                </button>
            </div>
        </div>
    `;

  if (elements.content) {
    elements.content.innerHTML = errorHtml;
  }
}

// ==========================================
// Auto-reload Detection
// ==========================================
function initAutoReload() {
  // Check for updates every 5 minutes
  setInterval(async () => {
    if (document.hidden) return; // Don't reload if tab is not visible

    const timeSinceLastFetch = Date.now() - (state.lastFetch || 0);
    if (timeSinceLastFetch > state.cacheExpiry) {
      console.log("🔄 Checking for documentation updates...");
      try {
        await loadAllContent();
        console.log("✅ Documentation refreshed");
      } catch (error) {
        console.error("❌ Failed to refresh documentation:", error);
      }
    }
  }, 5 * 60 * 1000); // Every 5 minutes
}

// Start auto-reload only in production
if (
  window.location.hostname !== "localhost" &&
  window.location.hostname !== "127.0.0.1"
) {
  initAutoReload();
}

// ==========================================
// Debug & Development Tools
// ==========================================
window.MosbergAPIDocs = {
  state,
  CONFIG,
  reload: loadAllContent,
  reloadStats: loadGitHubStats,
  version: "1.0.0",
  debug: {
    showState: () => console.log("Current state:", state),
    showConfig: () => console.log("Configuration:", CONFIG),
    testSearch: (query) => performSearch(query),
    clearCache: () => {
      state.lastFetch = 0;
      console.log("Cache cleared");
    },
  },
};

console.log("💡 Debug tools available via window.MosbergAPIDocs");
