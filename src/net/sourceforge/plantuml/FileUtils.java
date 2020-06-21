/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 */
package net.sourceforge.plantuml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.security.SFile;

// Used by the Eclipse Plugin, so do not change package location.
public class FileUtils {

	private static AtomicInteger counter;

	public static void resetCounter() {
		counter = new AtomicInteger(0);
	}

	static public File createTempFileLegacy(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false) {
			throw new IllegalArgumentException();
		}
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		final File f;
		if (counter == null) {
			f = File.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new File(name);
		}
		Log.info("Creating temporary file: " + f);
		f.deleteOnExit();
		return f;
	}

	static public SFile createTempFile(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false) {
			throw new IllegalArgumentException();
		}
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		final SFile f;
		if (counter == null) {
			f = SFile.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new SFile(name);
		}
		Log.info("Creating temporary file: " + f);
		f.deleteOnExit();
		return f;
	}

	private static void copyInternal(final InputStream fis, final OutputStream fos) throws IOException {
		final byte[] buf = new byte[10240];
		int len;
		while ((len = fis.read(buf)) > 0) {
			fos.write(buf, 0, len);
		}
		fos.close();
		fis.close();
	}

	static public void copyToFile(SFile src, SFile dest) throws IOException {
		if (dest.isDirectory()) {
			dest = dest.file(src.getName());
		}
		final InputStream fis = src.openFile();
		if (fis == null) {
			throw new FileNotFoundException();
		}
		final OutputStream fos = dest.createBufferedOutputStream();
		copyInternal(fis, fos);
	}

	static public void copyToStream(SFile src, OutputStream os) throws IOException {
		final InputStream fis = src.openFile();
		if (fis == null) {
			throw new FileNotFoundException();
		}
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToStream(File src, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToStream(InputStream is, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(is);
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos);
	}

	static public void copyToFile(byte[] src, SFile dest) throws IOException {
		final OutputStream fos = dest.createBufferedOutputStream();
		fos.write(src);
		fos.close();
	}

	static public String readSvg(SFile svgFile) throws IOException {
		final BufferedReader br = svgFile.openBufferedReader();
		if (br == null) {
			return null;
		}
		return readSvg(br, false, true);
	}

	static public String readSvg(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return readSvg(br, false, false);
	}

	static public String readFile(SFile svgFile) throws IOException {
		final BufferedReader br = svgFile.openBufferedReader();
		if (br == null) {
			return null;
		}
		return readSvg(br, true, true);
	}

	private static String readSvg(BufferedReader br, boolean withNewline, boolean withClose) throws IOException {
		final StringBuilder sb = new StringBuilder();
		String s;
		while ((s = br.readLine()) != null) {
			sb.append(s);
			if (withNewline) {
				sb.append("\n");
			}
		}
		if (withClose) {
			br.close();
		}
		return sb.toString();
	}

}
