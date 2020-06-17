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
package net.sourceforge.plantuml.creole.command;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.creole.Parser;
import net.sourceforge.plantuml.creole.legacy.StripeSimple;
import net.sourceforge.plantuml.graphic.Splitter;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class CommandCreoleOpenIcon implements Command {

	private final Pattern2 pattern;
	private final HColorSet colorSet;

	private CommandCreoleOpenIcon(HColorSet colorSet, String p) {
		this.pattern = MyPattern.cmpile(p);
		this.colorSet = colorSet;
	}

	public static Command create(HColorSet colorSet) {
		return new CommandCreoleOpenIcon(colorSet, "^(?i)(" + Splitter.openiconPattern + ")");
	}

	public int matchingSize(String line) {
		final Matcher2 m = pattern.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(1).length();
	}

	public String executeAndGetRemaining(String line, StripeSimple stripe) {
		final Matcher2 m = pattern.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final String src = m.group(2);
		final double scale = Parser.getScale(m.group(3), 1);
		final String colorName = Parser.getColor(m.group(3));
		HColor color = null;
		if (colorName != null) {
			color = colorSet.getColorIfValid(colorName);
		}
		stripe.addOpenIcon(src, scale, color);
		return line.substring(m.group(1).length());
	}

}