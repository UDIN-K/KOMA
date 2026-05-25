# KOMA Project — Agent Skill File

> **Last updated:** 2026-05-14
> **Maintainer:** UDIN-K (safrisam.id09@gmail.com)

## ⚠️ CRITICAL: Read This First

### GitHub Account Status
- The GitHub account **UDIN-K** was **flagged as "spammy"** on ~May 11, 2026.
- **Root cause:** A GitHub Actions workflow (`auto-sync.yml`) running every 2 minutes (`*/2 * * * *`) that force-pushed fake download metrics. This violated GitHub ToS Section 4 (Spam/Inauthentic Activity).
- The workflow has been **deleted** locally but the flag appeal is pending.
- **DO NOT** create any scheduled workflows with intervals less than 6 hours.
- **DO NOT** create any workflow that inflates metrics, fakes download counts, or auto-generates commits.
- **DO NOT** push to `origin` — it will fail until the account is restored.
- The repo has a **backup on Codeberg** (see conversation `a63ed518`).

### Sensitive Files — NEVER Commit These
The following files exist in the project root but are in `.gitignore`. **Never remove them from `.gitignore`, never reference their contents, never commit them:**
- `koma-release.jks` — Release signing keystore
- `keystore.properties` — Keystore credentials
- `client_secret_*.json` — Google OAuth secrets
- `google-services.json` — Firebase config
- `local.properties` — SDK path

---

## Project Identity

| Field | Value |
|-------|-------|
| **App Name** | KOMA |
| **Package ID** | `com.koma.reader` |
| **Current Version** | `1.0.7` (versionCode `83`) |
| **Base Fork** | Mihon / TachiyomiSY |
| **Namespace** | `eu.kanade.tachiyomi` (inherited from upstream) |
| **Min SDK** | See `app/build.gradle.kts` |
| **Language** | Kotlin, Jetpack Compose |
| **Build System** | Gradle (Kotlin DSL) |
| **Repo Finder URL** | `https://koma.udink.me/koma/repos.json` |

### Module Structure
```
KOMA/
├── app/              # Main Android application
├── core/             # Core utilities
├── core-metadata/    # Metadata handling
├── data/             # Data layer (database, network)
├── domain/           # Domain models and use cases
├── i18n/             # Translations (moko-resources)
├── i18n-sy/          # SY-specific translations
├── presentation-core/  # Shared Compose components
├── presentation-widget/ # Home screen widgets
├── source-api/       # Source/extension API
├── source-local/     # Local manga source
└── macrobenchmark/   # Performance benchmarks
```

---

## Code Conventions

### Namespace Mismatch (Important!)
The package ID is `com.koma.reader` but all source code lives under `eu.kanade.tachiyomi.*` and `tachiyomi.*`. This is intentional — it's inherited from the upstream fork. **Do NOT try to refactor package names.**

### Key Directories
- **UI/Compose:** `app/src/main/java/eu/kanade/presentation/`
- **Screen Models:** `app/src/main/java/eu/kanade/tachiyomi/ui/`
- **Domain Layer:** `domain/src/main/java/tachiyomi/domain/`
- **Data Layer:** `data/src/main/java/tachiyomi/data/`
- **Translations:** `i18n/src/commonMain/moko-resources/<locale>/strings.xml`
- **Compose Core:** `presentation-core/src/main/java/tachiyomi/presentation/core/`

### Build & Run
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (requires keystore)
./gradlew assembleRelease

# Run lint checks
./gradlew lintDebug
```

### Gradle Dependency Format
Use **single-string notation** (the multi-string format is deprecated):
```kotlin
// ✅ Correct
implementation("group:artifact:version")

// ❌ Wrong (deprecated)
implementation(group = "group", name = "artifact", version = "version")
```

---

## Feature Status (v1.0.7)

### ✅ Completed
- Rebranding (Mihon/TachiyomiSY → KOMA)
- Package ID change to `com.koma.reader`
- Custom themes and color scheme
- Library Source Badge (toggle in Library Settings)
- Repo Finder pointing to `https://koma.udink.me/koma/repos.json`
- i18n: 30+ languages via Weblate integration
- Changelog (`app/src/main/res/raw/changelog_release.xml`)
- Privacy Policy (`PRIVACY.md`)
- Release signing configuration

### 🔧 In Progress / Planned Polish
- Sticky headers for History tab
- Auto-hide FAB on scroll
- Shimmer loading effects for library grid images
- General UI polish pass

---

## Git Rules — FOLLOW STRICTLY

### Commit Messages
Use conventional commits format:
```
feat(library): add source badge toggle
fix(i18n): correct escape sequences in locale files
chore: update dependencies
docs: update README badges
```

### Translation Updates
- **ALWAYS** squash all i18n changes into a **single commit**
- Never push 5+ separate commits for language file updates
- Use descriptive messages: `fix(i18n): correct escape sequences in 23 locale files`
- Wait at least 1-2 hours between large pushes

### Branching
- Main branch: `main`
- No feature branches currently (single developer)

---

## Known Gotchas

1. **`bundletool.jar` (32MB)** exists in project root — it's in `.gitignore`, don't commit it
2. **Stale files in root:** `Task.txt`, `export tool.txt`, `scratch.kt`, `missing_batch*.json` — these are working files, ignore them
3. **`Could`, `Failed`, `Get`, `Learn`, `Run`** — empty/broken files in project root (likely from a failed command). Ignore these.
4. **The download badge in README** now uses real shields.io API (`github/downloads/UDIN-K/KOMA/total`), not a fake JSON endpoint. Don't revert this.
5. **`sync-releases.yml`** is the only remaining workflow — it's safe (triggers only on GitHub Release events)
6. **Library preferences** include `showSourceBadge` — added in v1.0.7 via `LibraryPreferences.kt`

---

## Important Files Quick Reference

| Purpose | File |
|---------|------|
| App version | `app/build.gradle.kts` (line ~37-38) |
| Library grid | `app/.../presentation/library/components/LibraryCompactGrid.kt` |
| Library badges | `app/.../presentation/library/components/LibraryBadges.kt` |
| Library settings | `app/.../presentation/library/LibrarySettingsDialog.kt` |
| Library screen model | `app/.../tachiyomi/ui/library/LibraryScreenModel.kt` |
| Repo Finder | `app/.../presentation/more/settings/screen/browse/RepoFinderScreenModel.kt` |
| History screen | `app/.../presentation/history/HistoryScreen.kt` |
| Changelog | `app/src/main/res/raw/changelog_release.xml` |
| Release model | `domain/.../domain/release/model/Release.kt` |
| Base strings | `i18n/src/commonMain/moko-resources/base/strings.xml` |
| Preferences | `domain/.../domain/library/service/LibraryPreferences.kt` |

---

## Infrastructure

| Service | URL / Details |
|---------|---------------|
| GitHub (flagged) | `https://github.com/UDIN-K/KOMA` |
| Codeberg (backup) | Check conversation `a63ed518` for URL |
| Repo Finder | `https://koma.udink.me/koma/repos.json` |
| Weblate (i18n) | Integrated for translation management |
| Firebase | Configured (see `google-services.json` in `.gitignore`) |

---

## What NOT To Do

1. ❌ **Don't create cron workflows** with intervals < 6 hours
2. ❌ **Don't inflate/fake any metrics** (downloads, stars, etc.)
3. ❌ **Don't refactor `eu.kanade.*` packages** to `com.koma.*`
4. ❌ **Don't commit secrets** (JKS, properties, JSON secrets)
5. ❌ **Don't make bulk i18n commits** (squash into one)
6. ❌ **Don't push to origin** until GitHub account flag is resolved
7. ❌ **Don't delete `.gitignore` entries** for sensitive files
8. ❌ **Don't use deprecated Gradle multi-string dependency notation**
