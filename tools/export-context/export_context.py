from __future__ import annotations

from datetime import datetime
from pathlib import Path


# ============================================================
# CONFIG
# ============================================================

SCRIPT_PATH = Path(__file__).resolve()

# tools/export-context/export_context.py -> repo root
REPO_ROOT = SCRIPT_PATH.parents[2]

OUTPUT_DIR = REPO_ROOT / "context_exports"

IGNORED_DIR_NAMES = {
    ".git",
    ".gradle",
    ".idea",
    ".vscode",
    "build",
    "out",
    "target",
    "node_modules",
    ".venv",
    "venv",
    "__pycache__",
    "context_exports",
    "docker-data",
    "redis-data",
}

IGNORED_FILE_NAMES = {
    ".DS_Store",
    "Thumbs.db",
}

ALLOWED_EXACT_FILE_NAMES = {
    ".gitignore",
    ".env.example",
}

ALLOWED_SUFFIXES = {
    ".md",
    ".java",
    ".kt",
    ".kts",
    ".gradle",
    ".toml",
    ".yml",
    ".yaml",
    ".json",
    ".py",
    ".ps1",
    ".sh",
    ".txt",
}

MAX_FILE_SIZE_BYTES = 250_000


# ============================================================
# FILTERING
# ============================================================

def should_skip_directory(path: Path) -> bool:
    name = path.name

    if name in IGNORED_DIR_NAMES:
        return True

    if name.startswith("-"):
        return True

    return False


def should_include_file(path: Path) -> bool:
    if path.name in IGNORED_FILE_NAMES:
        return False

    if path.name in ALLOWED_EXACT_FILE_NAMES:
        return True

    if path.suffix.lower() not in ALLOWED_SUFFIXES:
        return False

    if path.stat().st_size > MAX_FILE_SIZE_BYTES:
        return False

    return True


def discover_files(root: Path) -> list[Path]:
    included_files: list[Path] = []

    for current_dir, dir_names, file_names in root.walk():
        dir_names[:] = [
            name for name in dir_names
            if not should_skip_directory(current_dir / name)
        ]

        for file_name in file_names:
            file_path = current_dir / file_name

            if should_include_file(file_path):
                included_files.append(file_path)

    return sorted(included_files, key=lambda path: path.relative_to(root).as_posix())


# ============================================================
# EXPORT
# ============================================================

def read_file_safely(path: Path) -> str:
    try:
        return path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        return "[Skipped: file is not valid UTF-8 text]"
    except Exception as error:
        return f"[Skipped: could not read file: {error}]"


def fence_language(path: Path) -> str:
    if path.name == ".gitignore":
        return "gitignore"

    if path.name == ".env.example":
        return "dotenv"

    suffix = path.suffix.lower()

    return {
        ".md": "md",
        ".java": "java",
        ".kt": "kotlin",
        ".kts": "kotlin",
        ".gradle": "gradle",
        ".toml": "toml",
        ".yml": "yaml",
        ".yaml": "yaml",
        ".json": "json",
        ".py": "python",
        ".ps1": "powershell",
        ".sh": "bash",
        ".txt": "text",
    }.get(suffix, "text")


def build_export_markdown(files: list[Path]) -> str:
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    lines: list[str] = [
        "# WorldMesh Context Export",
        "",
        f"Generated: `{timestamp}`",
        f"Repository root: `{REPO_ROOT}`",
        "",
        "## Summary",
        "",
        "This file contains selected source, configuration, documentation, and tooling files from the WorldMesh repository.",
        "",
        "Bulky folders, generated outputs, binary files, virtual environments, dependency folders, and directories starting with `-` are excluded.",
        "",
        "## Manifest",
        "",
    ]

    for file_path in files:
        relative_path = file_path.relative_to(REPO_ROOT).as_posix()
        size = file_path.stat().st_size
        lines.append(f"- `{relative_path}` ({size} bytes)")

    lines.extend([
        "",
        "## File Contents",
        "",
    ])

    for file_path in files:
        relative_path = file_path.relative_to(REPO_ROOT).as_posix()
        language = fence_language(file_path)
        content = read_file_safely(file_path)

        lines.extend([
            f"### `{relative_path}`",
            "",
            f"```{language}",
            content.rstrip(),
            "```",
            "",
        ])

    return "\n".join(lines)


def main() -> None:
    OUTPUT_DIR.mkdir(exist_ok=True)

    files = discover_files(REPO_ROOT)

    timestamp = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    output_file = OUTPUT_DIR / f"worldmesh_context_{timestamp}.md"

    markdown = build_export_markdown(files)
    output_file.write_text(markdown, encoding="utf-8")

    print(f"Export complete: {output_file}")
    print(f"Files included: {len(files)}")


if __name__ == "__main__":
    main()