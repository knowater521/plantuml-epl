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

import java.io.IOException;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class FileSystem {

	private final static FileSystem singleton = new FileSystem();

	private final ThreadLocal<SFile> currentDir = new ThreadLocal<SFile>();

	private FileSystem() {
		reset();
	}

	public static FileSystem getInstance() {
		return singleton;
	}

	public void setCurrentDir(SFile dir) {
		// if (dir == null) {
		// throw new IllegalArgumentException();
		// }
		if (dir != null) {
			Log.info("Setting current dir: " + dir.getAbsolutePath());
		}
		this.currentDir.set(dir);
	}

	public SFile getCurrentDir() {
		return this.currentDir.get();
	}

	public SFile getFile(String nameOrPath) throws IOException {
		if (isAbsolute(nameOrPath)) {
			return new SFile(nameOrPath).getCanonicalFile();
		}
		final SFile dir = currentDir.get();
		SFile filecurrent = null;
		if (dir != null) {
			filecurrent = dir.getAbsoluteFile().file(nameOrPath);
			if (filecurrent.exists()) {
				return filecurrent.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("plantuml.include.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("java.class.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();
			}
		}
		if (dir == null) {
			assert filecurrent == null;
			return new SFile(nameOrPath).getCanonicalFile();
		}
		assert filecurrent != null;
		return filecurrent;
	}

	private boolean isAbsolute(String nameOrPath) {
		final SFile f = new SFile(nameOrPath);
		return f.isAbsolute();
	}

	public void reset() {
		setCurrentDir(new SFile("."));
	}

}
