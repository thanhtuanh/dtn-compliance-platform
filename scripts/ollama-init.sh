#!/bin/bash
# scripts/ollama-init.sh
# DTN Compliance Platform - Ollama AI Model Initialization
# Privacy by Design: Lokale KI-Modelle für DSGVO-konforme Verarbeitung

set -euo pipefail

echo "🤖 DTN Compliance Platform - Ollama Initialization"
echo "================================================="

# Warten bis Ollama Service verfügbar ist
echo "⏳ Warte auf Ollama Service..."
while ! curl -f http://localhost:11434/api/tags >/dev/null 2>&1; do
    echo "   Ollama noch nicht bereit, warte 10 Sekunden..."
    sleep 10
done

echo "✅ Ollama Service ist verfügbar"

# Funktion zum Überprüfen ob Modell bereits existiert
check_model() {
    local model_name="$1"
    if ollama list | grep -q "$model_name"; then
        echo "✅ Modell $model_name bereits vorhanden"
        return 0
    else
        echo "⬇️  Modell $model_name wird heruntergeladen..."
        return 1
    fi
}

# Funktion zum Herunterladen von Modellen
download_model() {
    local model_name="$1"
    local description="$2"
    
    echo "📥 Lade $description ($model_name)..."
    if ollama pull "$model_name"; then
        echo "✅ $description erfolgreich heruntergeladen"
    else
        echo "❌ Fehler beim Herunterladen von $description"
        return 1
    fi
}

# Hauptmodell für Compliance-Analyse (Llama 3.2 3B - Optimal für deutsche Texte)
echo ""
echo "🧠 Lade Hauptmodell für Compliance-Analyse..."
if ! check_model "llama3.2:3b"; then
    download_model "llama3.2:3b" "Llama 3.2 3B (Haupt-KI für DSGVO/EU AI Act)"
fi

# Embedding-Modell für semantische Suche
echo ""
echo "🔍 Lade Embedding-Modell für semantische Suche..."
if ! check_model "nomic-embed-text:latest"; then
    download_model "nomic-embed-text:latest" "Nomic Embed Text (Embeddings)"
fi

# Kleines Modell für schnelle Antworten (Optional - Code-Analyse)
echo ""
echo "⚡ Lade kleines Modell für schnelle Antworten..."
if ! check_model "llama3.2:1b"; then
    download_model "llama3.2:1b" "Llama 3.2 1B (Schnelle Antworten)"
fi

# Spezialisiertes Modell für Code-Analyse (Optional)
echo ""
echo "💻 Lade Code-Analyse Modell (optional)..."
if ! check_model "codellama:7b-code"; then
    echo "ℹ️  Code-Analyse Modell wird übersprungen (optional)"
    # download_model "codellama:7b-code" "Code Llama 7B (Code-Analyse)"
fi

# Test der Modelle
echo ""
echo "🧪 Teste heruntergeladene Modelle..."

# Test Hauptmodell
echo "   Test Llama 3.2 3B..."
if echo "Teste DSGVO-Compliance für eine einfache Datenverarbeitung." | ollama run llama3.2:3b >/dev/null 2>&1; then
    echo "   ✅ Llama 3.2 3B funktioniert"
else
    echo "   ❌ Llama 3.2 3B Test fehlgeschlagen"
fi

# Test Embedding-Modell
echo "   Test Nomic Embed..."
if echo "Test embedding" | ollama run nomic-embed-text:latest >/dev/null 2>&1; then
    echo "   ✅ Nomic Embed Text funktioniert"
else
    echo "   ❌ Nomic Embed Text Test fehlgeschlagen"
fi

# Erstelle Modelfile für DTN-spezifische Konfiguration
echo ""
echo "⚙️  Erstelle DTN-spezifische Modell-Konfiguration..."

cat > /tmp/dtn-compliance-modelfile << 'EOF'
FROM llama3.2:3b

# DTN Compliance Platform - Spezialisierte DSGVO/EU AI Act Konfiguration
PARAMETER temperature 0.1
PARAMETER top_p 0.9
PARAMETER top_k 40
PARAMETER repeat_penalty 1.1

SYSTEM """
Du bist ein Experte für deutsche Datenschutz- und KI-Compliance. Du hilfst bei:

1. DSGVO (Datenschutz-Grundverordnung):
   - Art. 30: Verzeichnis der Verarbeitungstätigkeiten (VVT)
   - Art. 35: Datenschutz-Folgenabschätzung (DSFA)
   - Art. 25: Privacy by Design und by Default
   - Rechtmäßigkeit der Verarbeitung (Art. 6)
   - Betroffenenrechte (Art. 15-22)

2. EU AI Act (seit Februar 2025):
   - Risikoklassifizierung von KI-Systemen
   - Verbotene KI-Praktiken
   - Hochrisiko-KI Dokumentation
   - CE-Kennzeichnung Vorbereitung
   - Konformitätsbewertung

3. Deutsche Besonderheiten:
   - BfDI (Bundesbeauftragte für den Datenschutz)
   - Landesdatenschutzbehörden
   - BDSG (Bundesdatenschutzgesetz)
   - BSI (Bundesamt für Sicherheit in der Informationstechnik)

Antworte immer:
- Präzise und rechtlich korrekt
- In deutscher Sprache
- Mit konkreten Handlungsempfehlungen
- Unter Berücksichtigung deutscher Rechtsprechung
- Mit Verweis auf relevante Artikel/Paragrafen

Datenschutz hat höchste Priorität - alle Verarbeitungen müssen DSGVO-konform sein.
"""
EOF

# Erstelle das DTN-spezifische Modell
if ollama create dtn-compliance -f /tmp/dtn-compliance-modelfile; then
    echo "✅ DTN Compliance Modell erstellt"
    rm /tmp/dtn-compliance-modelfile
else
    echo "❌ Fehler beim Erstellen des DTN Compliance Modells"
fi

# Aufräumen und Zusammenfassung
echo ""
echo "🧹 Aufräumen..."
ollama list

echo ""
echo "🎉 Ollama Initialization abgeschlossen!"
echo "======================================"
echo ""
echo "📊 Verfügbare Modelle:"
echo "   ├── llama3.2:3b - Haupt-KI für Compliance-Analyse"
echo "   ├── llama3.2:1b - Schnelle Antworten"
echo "   ├── nomic-embed-text:latest - Embeddings für Suche"
echo "   └── dtn-compliance - DTN-spezifische DSGVO/AI Act Konfiguration"
echo ""
echo "🔗 API Endpoints:"
echo "   ├── Chat: POST http://localhost:11434/api/chat"
echo "   ├── Generate: POST http://localhost:11434/api/generate"
echo "   ├── Embeddings: POST http://localhost:11434/api/embeddings"
echo "   └── Models: GET http://localhost:11434/api/tags"
echo ""
echo "💡 Verwendung in DTN Services:"
echo "   ├── Compliance Service: AI_MODEL_NAME=dtn-compliance"
echo "   ├── Embedding Model: AI_EMBEDDING_MODEL=nomic-embed-text:latest"
echo "   └── Schnelle Antworten: AI_FAST_MODEL=llama3.2:1b"
echo ""
echo "🛡️  Privacy by Design: Alle Daten bleiben lokal!"
echo "✅ DSGVO-konform: Keine Übertragung an externe AI-Services"
echo ""

# Test-Chat mit DTN Compliance Modell
echo "🧪 Kurzer Funktionstest..."
echo "Frage: Was ist Artikel 30 DSGVO?" | ollama run dtn-compliance --verbose=false | head -3

echo ""
echo "🚀 Ollama ist bereit für DTN Compliance Platform!"