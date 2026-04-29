package com.peerlink.session;

public class SessionPayload {

    private int videoPort;
    private int audioPort;
    private long videoSsrc;
    private long audioSsrc;

    public SessionPayload(int videoPort, int audioPort, long videoSsrc, long audioSsrc) {
        this.videoPort = videoPort;
        this.audioPort = audioPort;
        this.videoSsrc = videoSsrc;
        this.audioSsrc = audioSsrc;
    }

    public int getVideoPort() { return videoPort; }
    public int getAudioPort() { return audioPort; }
    public long getVideoSsrc() { return videoSsrc; }
    public long getAudioSsrc() { return audioSsrc; }

    // ── Encode: object → string ─────────────────────────────
    public String encode() {
        return "SESSION:" + videoPort + ":" + audioPort + ":" + videoSsrc + ":" + audioSsrc;
    }

    // ── Decode: string → object ─────────────────────────────
    public static SessionPayload decode(String raw) {
        if (raw == null) return null;

        String[] parts = raw.split(":");
        if (parts.length != 5 || !parts[0].equals("SESSION")) {
            throw new IllegalArgumentException("Invalid SESSION format: " + raw);
        }

        int videoPort = Integer.parseInt(parts[1]);
        int audioPort = Integer.parseInt(parts[2]);
        long videoSsrc = Long.parseLong(parts[3]);
        long audioSsrc = Long.parseLong(parts[4]);

        return new SessionPayload(videoPort, audioPort, videoSsrc, audioSsrc);
    }

    @Override
    public String toString() {
        return "SessionPayload{" +
                "videoPort=" + videoPort +
                ", audioPort=" + audioPort +
                ", videoSsrc=" + videoSsrc +
                ", audioSsrc=" + audioSsrc +
                '}';
    }
}