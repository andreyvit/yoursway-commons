package com.yoursway.utils.storage.atomic;

import static com.yoursway.utils.io.HashingInputStreamDecorator.md5HashingDecorator;
import static com.yoursway.utils.io.StreamCloseBehavior.IGNORE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import com.yoursway.utils.YsDigest;
import com.yoursway.utils.io.HashingInputStreamDecorator;
import com.yoursway.utils.io.InputStreamConsumer;
import com.yoursway.utils.io.OutputStreamConsumer;
import com.yoursway.utils.io.RandomAccessFileInputStream;
import com.yoursway.utils.io.RandomAccessFileOutputStream;

public class AtomicFile {
    
    private final File file;
    
    public AtomicFile(File file) {
        if (file == null)
            throw new NullPointerException("file is null");
        this.file = file;
    }
    
    public AtomicFileState read(InputStreamConsumer consumer) throws IOException {
        FileInputStream in = new FileInputStream(file);
        String hash;
        try {
            FileLock lock = in.getChannel().lock(0L, Long.MAX_VALUE, true);
            try {
                HashingInputStreamDecorator in2 = md5HashingDecorator(in, IGNORE);
                consumer.run(in2);
                hash = in2.getHexHash();
            } finally {
                lock.release();
            }
        } finally {
            in.close();
        }
        return new AtomicFileStateImpl(hash);
    }
    
    class AtomicFileStateImpl implements AtomicFileState {
        
        private final String hash;
        
        public AtomicFileStateImpl(String hash) {
            if (hash == null)
                throw new NullPointerException("hash is null");
            this.hash = hash;
        }
        
        public void write(OutputStreamConsumer consumer) throws IOException,
                ConcurrentFileModificationException {
            RandomAccessFile ra = new RandomAccessFile(file, "rw");
            try {
                FileLock lock = ra.getChannel().lock();
                try {
                    String hash = YsDigest.md5(new RandomAccessFileInputStream(ra, IGNORE));
                    if (!hash.equals(this.hash))
                        throw new ConcurrentFileModificationException(file, this.hash, hash);
                    ra.seek(0);
                    consumer.run(new RandomAccessFileOutputStream(ra, IGNORE));
                    ra.setLength(ra.getFilePointer());
                } finally {
                    lock.release();
                }
            } finally {
                ra.close();
            }
        }
        
    }
    
    public void overwrite(OutputStreamConsumer consumer) throws IOException {
        RandomAccessFile ra = new RandomAccessFile(file, "rw");
        try {
            FileLock lock = ra.getChannel().lock();
            try {
                consumer.run(new RandomAccessFileOutputStream(ra, IGNORE));
                ra.setLength(ra.getFilePointer());
            } finally {
                lock.release();
            }
        } finally {
            ra.close();
        }
    }
    
}
