"""Convertit le rendu Markdown d'un lot en PDF lisible (corrigés dépliés dans un encadré), via Edge ou Chromium headless.

Usage : python generation/pdf.py <fichier.md>   → produit <fichier>.html et <fichier>.pdf à côté
"""
import shutil
import subprocess
import sys
from pathlib import Path

import markdown

# Edge sous Windows ; Chromium ailleurs (celui de Playwright dans l'environnement cloud).
NAVIGATEURS = [r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
               "/opt/pw-browsers/chromium", shutil.which("chromium"), shutil.which("chromium-browser")]
NAVIGATEUR = next((n for n in NAVIGATEURS if n and Path(n).is_file()), None)
CSS = """
@page { size: A4; margin: 14mm 13mm; }
body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 10.5pt; line-height: 1.45; color: #1a1a1a; }
h1 { font-size: 18pt; color: #1f3a5f; } h2 { font-size: 14pt; color: #1f3a5f; border-bottom: 2px solid #1f3a5f; padding-bottom: 3px; margin-top: 0; }
table { border-collapse: collapse; margin: 4px 0 8px; font-size: 9.5pt; } td, th { border: 1px solid #c8c8c8; padding: 2px 8px; } th { background: #eef2f7; }
.corrige { background: #f3f7f2; border-left: 4px solid #3c7a3c; padding: 4px 10px; margin: 4px 0 12px; font-size: 9.5pt; }
.corrige .titre { font-weight: 700; color: #3c7a3c; margin: 2px 0; }
sub { font-size: 7.5pt; color: #666; vertical-align: baseline; }
.saut { page-break-after: always; }
"""


def main(source):
    source = Path(source).resolve()
    md = source.read_text(encoding="utf-8")
    md = md.replace("<details><summary>Corrigé</summary>",
                    '<div class="corrige" markdown="1">\n<p class="titre">Corrigé</p>\n').replace("</details>", "</div>")
    md = md.replace("\n---\n", '\n<div class="saut"></div>\n')
    corps = markdown.markdown(md, extensions=["tables", "md_in_html"])
    html = source.with_suffix(".html")
    html.write_text(f'<!DOCTYPE html><html lang="fr"><head><meta charset="utf-8"><title>{source.stem}</title>'
                    f"<style>{CSS}</style></head><body>{corps}</body></html>", encoding="utf-8")
    pdf = source.with_suffix(".pdf")
    if NAVIGATEUR is None:
        sys.exit("Aucun navigateur trouvé (Edge ou Chromium) pour produire le PDF.")
    subprocess.run([NAVIGATEUR, "--headless", "--disable-gpu", "--no-sandbox", "--no-pdf-header-footer",
                    f"--print-to-pdf={pdf}", html.as_uri()], check=True, capture_output=True)
    print(f"{pdf} ({pdf.stat().st_size // 1024} Ko)")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print(__doc__)
        sys.exit(2)
    main(sys.argv[1])
