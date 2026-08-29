package fastaiguard;

import fastaiguard.pii.PIIMasker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class FastAIGuard {

    private static final List<String> INJECTION_PATTERNS = List.of(
        "ignore previous instructions",
        "ignore all previous instructions",
        "disregard all instructions",
        "you are now in developer mode",
        "dan mode enabled",
        "system prompt override",
        "bypass safety guidelines",
        "reveal system prompt",
        "print your original instructions",
        "sudo rm -rf",
        "drop table",
        "<script>",
        "exec(base64"
    );

    private final Set<String> allowedTools = new HashSet<>();
    private boolean maskPii = true;
    private boolean strictMode = false;

    public FastAIGuard() {
        this.allowedTools.addAll(Arrays.asList("read_file", "search_web", "list_dir", "get_weather", "calculator"));
    }

    public FastAIGuard allowTool(String toolName) {
        if (toolName != null) allowedTools.add(toolName.toLowerCase(Locale.ROOT));
        return this;
    }

    public FastAIGuard setMaskPii(boolean enable) {
        this.maskPii = enable;
        return this;
    }

    public FastAIGuard setStrictMode(boolean enable) {
        this.strictMode = enable;
        return this;
    }

    /**
     * Inspects inbound user prompts or third-party webhooks before passing them to the LLM.
     */
    public GuardResult inspectPrompt(String prompt) {
        long start = System.nanoTime();
        if (prompt == null || prompt.isEmpty()) {
            return new GuardResult(true, prompt, Collections.emptyList(), System.nanoTime() - start);
        }

        String lower = prompt.toLowerCase(Locale.ROOT);
        List<String> violations = new ArrayList<>();

        for (String inj : INJECTION_PATTERNS) {
            if (lower.contains(inj)) {
                violations.add("PROMPT_INJECTION_DETECTED: " + inj);
            }
        }

        String sanitized = prompt;
        if (maskPii) {
            sanitized = PIIMasker.maskAll(sanitized);
        }

        boolean passed = violations.isEmpty();
        long latency = System.nanoTime() - start;
        return new GuardResult(passed, sanitized, violations, latency);
    }

    /**
     * Verifies outbound tool calls from LLMs before execution.
     */
    public GuardResult inspectToolCall(String toolName, String payload) {
        long start = System.nanoTime();
        List<String> violations = new ArrayList<>();

        if (toolName == null || !allowedTools.contains(toolName.toLowerCase(Locale.ROOT))) {
            violations.add("UNAUTHORIZED_TOOL_EXECUTION: " + toolName);
        }

        if (payload != null) {
            String lower = payload.toLowerCase(Locale.ROOT);
            if (lower.contains("rm -rf") || lower.contains("format c:") || lower.contains("mkfs")) {
                violations.add("DANGEROUS_COMMAND_PAYLOAD: Destructive OS operation blocked");
            }
        }

        boolean passed = violations.isEmpty();
        long latency = System.nanoTime() - start;
        return new GuardResult(passed, payload, violations, latency);
    }
}