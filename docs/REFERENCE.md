# FastAIGuard Reference & API Specification

## 1. Core Vocabulary

*   **Prompt Injection**: Malicious input sequences designed to override system prompts or hijack model instructions (e.g. DAN jailbreaks, "ignore previous instructions").
*   **PII (Personally Identifiable Information)**: Sensitive customer data including credit cards, email addresses, phone numbers, and API tokens.
*   **Tool-Call Whitelist**: A strict, pre-approved registry of allowed agent actions preventing unauthorized OS commands or database manipulation.
*   **Deterministic Firewall**: High-speed, non-probabilistic regex/pattern scanners that execute with zero hallucination and microsecond latencies.

## 2. API Quick Reference

### `FastAIGuard` (Main Facade)
*   `FastAIGuard()`: Instantiates the AI guardrail firewall with default tool whitelists.
*   `inspectPrompt(String prompt)`: Inspects inbound text for injections and masks sensitive PII.
*   `inspectToolCall(String toolName, String payload)`: Validates tool permissions and blocks destructive payloads (`rm -rf`, `format`).
*   `allowTool(String toolName)`: Adds an approved tool name to the execution whitelist.
*   `setMaskPii(boolean enable)`: Toggles in-flight PII redaction.
*   `setStrictMode(boolean enable)`: Toggles strict heuristic enforcement.

### `GuardResult`
*   `isPassed()`: Returns `true` if no violations were found.
*   `isBlocked()`: Returns `true` if a threat or unauthorized tool was intercepted.
*   `getSanitizedText()`: Returns the PII-masked text.
*   `getViolations()`: Returns list of identified threat descriptions.
*   `getLatencyMicros()`: Returns the execution duration in microseconds.

---
**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*