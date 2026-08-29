package fastaiguard;

import java.util.Collections;
import java.util.List;

public final class GuardResult {
    private final boolean passed;
    private final String sanitizedText;
    private final List<String> violations;
    private final long latencyNanos;

    public GuardResult(boolean passed, String sanitizedText, List<String> violations, long latencyNanos) {
        this.passed = passed;
        this.sanitizedText = sanitizedText;
        this.violations = violations != null ? Collections.unmodifiableList(violations) : Collections.emptyList();
        this.latencyNanos = latencyNanos;
    }

    public boolean isPassed() { return passed; }
    public boolean isBlocked() { return !passed; }
    public String getSanitizedText() { return sanitizedText; }
    public List<String> getViolations() { return violations; }
    public long getLatencyNanos() { return latencyNanos; }
    public double getLatencyMicros() { return latencyNanos / 1000.0; }
}