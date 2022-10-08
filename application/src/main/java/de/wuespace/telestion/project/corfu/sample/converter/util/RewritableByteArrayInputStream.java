package de.wuespace.telestion.project.corfu.sample.converter.util;

import java.io.ByteArrayInputStream;

public class RewritableByteArrayInputStream extends ByteArrayInputStream {
	public RewritableByteArrayInputStream(byte[] buf) {
		super(buf);
	}

	public RewritableByteArrayInputStream(byte[] buf, int offset, int length) {
		super(buf, offset, length);
	}

	@Override
	public void reset() {
		super.reset();
	}

	public void reset(byte[] buf) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	public void reset(byte[] buf, int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.mark = offset;
		this.count = Math.min(offset + length, buf.length);
	}

	public int getCurrentPos() {
		return pos;
	}

	public byte[] getBuffer() {
		return buf;
	}
}
