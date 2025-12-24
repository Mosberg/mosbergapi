# 🔧 Core Java and Gradle Extensions

### **1. Gradle for Java (Microsoft)**

- Run tasks, view dependencies, inspect Gradle projects
- Essential for Fabric Loom builds
- Great for quickly triggering remaps, builds, and test tasks

### **2. Java Extension Pack (Microsoft)**

Includes:

- Language Support for Java
- Debugger for Java
- Test Runner
- Maven/Gradle tools  
  Perfect baseline for any Fabric modding environment.

### **3. Lombok Annotations Support**

- If you ever use Lombok in helper modules, this prevents red squiggles and broken IntelliSense.

---

# 🧵 Minecraft / Fabric‑Specific Extensions

### **4. Fabric Language Kotlin (if you ever mix Kotlin)**

- Not required for your current project, but useful if you expand into Kotlin utilities.

### **5. Minecraft Development Pack (Community)**

- Syntax highlighting for `.mcmeta`, loot tables, tags, advancements
- JSON schema validation for Minecraft data files
- Helps keep your API’s data-driven systems clean and error-free

### **6. JSON Editor / JSON Tools**

- Since Fabric mods rely heavily on JSON (tags, recipes, mixin configs), these help with:
  - Formatting
  - Validation
  - Schema-based autocomplete

---

# 🧬 Mixin & Bytecode Extensions

### **7. Mixin Syntax Highlighting**

- Adds proper highlighting for Mixin annotations
- Helps readability when working with injection points

### **8. ASM Helper (optional)**

- If you ever dive into bytecode-level utilities, this is a lifesaver.

---

# 🧪 Testing & Debugging Extensions

### **9. Test Runner for Java**

- Integrates JUnit 6 (your version)
- Lets you run tests directly from the editor

### **10. Mockito Snippets**

- Speeds up writing mocks for your API’s internal systems

### **11. Coverage Gutters**

- Visualizes test coverage directly in the editor
- Great for API-level validation

---

# 📦 Dependency & Build Insight

### **12. Gradle Dependency Graph**

- Generates a visual dependency tree
- Useful for debugging transitive Fabric dependencies

### **13. Version Lens**

- Shows latest versions of dependencies inline
- Helps keep Fabric, Gson, SLF4J, etc. up to date

---

# 🧹 Code Quality & Style

### **14. Checkstyle**

- Enforces consistent formatting across your API
- Great for contributor onboarding

### **15. SpotBugs**

- Static analysis for Java
- Helps catch nullability issues, threading mistakes, etc.

### **16. Error Lens**

- Makes errors/warnings extremely visible
- Perfect for fast iteration

---

# 🧭 Productivity & Workflow

### **17. GitLens**

- Deep Git history, blame, and PR integration
- Essential for maintaining a public API library

### **18. Path Intellisense**

- Autocomplete for file paths
- Useful when referencing assets or config files

### **19. Todo Tree**

- Surfaces TODO/FIXME across your mod
- Great for sprint planning

### **20. Better Comments**

- Lets you categorize comments (e.g., `// !`, `// ?`, `// *`)
- Helps structure your API documentation inline

---

# 🎨 Optional but Highly Useful for Modding

### **21. Icon Preview / Texture Preview**

- Instantly previews PNGs
- Great for item icons, UI textures, and sprite sheets

### **22. Color Highlight**

- Highlights hex colors in JSON or Java
- Useful for UI/FX utilities in your API
