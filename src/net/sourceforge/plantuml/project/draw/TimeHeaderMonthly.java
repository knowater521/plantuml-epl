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
package net.sourceforge.plantuml.project.draw;

import java.util.Map;

import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.GCalendar;
import net.sourceforge.plantuml.project.time.MonthYear;
import net.sourceforge.plantuml.project.time.Wink;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TimeHeaderMonthly extends TimeHeader {

	private double getTimeHeaderHeight() {
		return Y_POS_ROW16 + 13;
	}

	private final GCalendar calendar;

	public TimeHeaderMonthly(GCalendar calendar, Wink min, Wink max, LoadPlanable defaultPlan,
			Map<Day, HColor> colorDays, Map<Day, String> nameDays) {
		super(min, max, new TimeScaleCompressed(calendar, PrintScale.MONTHLY.getCompress()));
		this.calendar = calendar;
	}

	@Override
	public void drawTimeHeader(final UGraphic ug, double totalHeight) {
		drawCalendar(ug, totalHeight);
		drawHline(ug, 0);
		drawHline(ug, Y_POS_ROW16);
		drawHline(ug, getFullHeaderHeight());

	}

	private void drawCalendar(final UGraphic ug, double totalHeight) {
		drawYears(ug);
		drawMonths(ug);
	}

	private void drawYears(final UGraphic ug) {
		MonthYear last = null;
		double lastChange = -1;
		for (Wink wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final Day day = calendar.toDayAsDate(wink);
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (last == null || day.monthYear().year() != last.year()) {
				drawVbar(ug, x1, 0, Y_POS_ROW16);
				if (last != null) {
					printYear(ug, last, lastChange, x1);
				}
				lastChange = x1;
				last = day.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printYear(ug, last, lastChange, x1);
		}
	}

	private void drawMonths(final UGraphic ug) {
		MonthYear last = null;
		double lastChange = -1;
		for (Wink wink = min; wink.compareTo(max) < 0; wink = wink.increment()) {
			final Day day = calendar.toDayAsDate(wink);
			final double x1 = getTimeScale().getStartingPosition(wink);
			if (day.monthYear().equals(last) == false) {
				drawVbar(ug, x1, Y_POS_ROW16, Y_POS_ROW28);
				if (last != null) {
					printMonth(ug.apply(UTranslate.dy(Y_POS_ROW16)), last, lastChange, x1);
				}
				lastChange = x1;
				last = day.monthYear();
			}
		}
		final double x1 = getTimeScale().getStartingPosition(max.increment());
		if (x1 > lastChange) {
			printMonth(ug.apply(UTranslate.dy(Y_POS_ROW16)), last, lastChange, x1);
		}
	}

	private void printYear(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock("" + monthYear.year(), 12, true);
		printCentered(ug, start, end, small);
	}

	private void printMonth(UGraphic ug, MonthYear monthYear, double start, double end) {
		final TextBlock small = getTextBlock(monthYear.shortName(), 10, false);
		final TextBlock big = getTextBlock(monthYear.longName(), 10, false);
		printCentered(ug, start, end, small, big);
	}

	private void drawVbar(UGraphic ug, double x, double y1, double y2) {
		final ULine vbar = ULine.vline(y2 - y1);
		ug.apply(HColorUtils.LIGHT_GRAY).apply(new UTranslate(x, y1)).draw(vbar);
	}

	private void printLeft(UGraphic ug, TextBlock text, double start) {
		text.drawU(ug.apply(UTranslate.dx(start)));
	}

	@Override
	public double getFullHeaderHeight() {
		return getTimeHeaderHeight();
	}

}
