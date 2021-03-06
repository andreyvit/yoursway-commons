package com.yoursway.utils;

import static com.google.common.base.Functions.TO_STRING;
import static com.google.common.base.Join.join;
import static com.google.common.collect.Iterators.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YsStrings {

	public static String replace(String original, int start, int length,
			String replacement) {
		StringBuilder builder = new StringBuilder(original.length() - length
				+ replacement.length());
		if (start > 0)
			builder.append(original.substring(0, start));
		builder.append(replacement);
		if (start + length < original.length())
			builder.append(original.substring(start + length));
		return builder.toString();
	}

	public static String emptyToNullWithTrim(String string) {
		if (string == null || (string = string.trim()).length() == 0)
			return null;
		return string;
	}

	public static String emptyToNull(String string) {
		if (string == null || string.length() == 0)
			return null;
		return string;
	}

	public static String nullToEmpty(String string) {
		if (string == null)
			return "";
		return string;
	}

	public static Comparator<Object> toStringComparator(
			final Comparator<? super String> cmp) {
		return new Comparator<Object>() {

			public int compare(Object o1, Object o2) {
				return cmp.compare(o1.toString(), o2.toString());
			}

		};
	}

	public static <T> String sortedToStringUsing(Iterable<T> iterable,
			String delimiter) {
		return sortedToStringUsing(iterable.iterator(), delimiter);
	}

	public static <T> String sortedToStringUsing(Iterator<T> iterator,
			String delimiter) {
		return join(delimiter, YsCollections.sort(toStringList(iterator)));
	}

	public static <T> String sortedToStringWithNewLines(Iterable<T> iterable) {
		return sortedToStringUsing(iterable, "\n");
	}

	public static <T> String sortedToStringWithNewLines(Iterator<T> iterator) {
		return sortedToStringUsing(iterator, "\n");
	}

	public static String sortedToString(Iterable<?> collection) {
		return sortedToString(collection.iterator());
	}

	public static String sortedToString(Iterator<?> iterator) {
		List<String> list = newArrayList(toStringList(iterator));
		Collections.sort(list);
		return list.toString();
	}

	public static Iterator<String> toStringList(Iterator<?> iterator) {
		return transform(iterator, TO_STRING);
	}

	public static Iterator<String> toStringList(Iterable<?> iterable) {
		return toStringList(iterable.iterator());
	}

	public static <T extends Comparable<T>> List<T> sortedCopy(
			Iterable<T> iterable) {
		return YsCollections.sort(iterable);
	}

	public static <T extends Comparable<T>> List<T> sortedCopy(
			Iterator<T> iterator) {
		return YsCollections.sort(iterator);
	}

	/**
	 * <p>
	 * A string comparator that does case sensitive comparisons and handles
	 * embedded numbers correctly.
	 * </p>
	 * <p>
	 * <b>Do not use</b> if your app might ever run on any locale that uses more
	 * than 7-bit ascii characters.
	 * </p>
	 */
	private static final Comparator<String> NATURAL_COMPARATOR_ASCII = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return compareNaturalAscii(o1, o2);
		}
	};

	/**
	 * <p>
	 * A string comparator that does case insensitive comparisons and handles
	 * embedded numbers correctly.
	 * </p>
	 * <p>
	 * <b>Do not use</b> if your app might ever run on any locale that uses more
	 * than 7-bit ascii characters.
	 * </p>
	 */
	private static final Comparator<String> IGNORE_CASE_NATURAL_COMPARATOR_ASCII = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return compareNaturalIgnoreCaseAscii(o1, o2);
		}
	};

	/**
	 * This is a utility class (static methods only), don't instantiate.
	 */
	private YsStrings() {
	}

	/**
	 * Returns a comparator that compares contained numbers based on their
	 * numeric values and compares other parts using the current locale's order
	 * rules.
	 * <p>
	 * For example in German locale this will be a comparator that handles
	 * umlauts correctly and ignores upper/lower case differences.
	 * </p>
	 * 
	 * @return <p>
	 *         A string comparator that uses the current locale's order rules
	 *         and handles embedded numbers correctly.
	 *         </p>
	 * @see #getNaturalComparator(java.text.Collator)
	 */
	public static Comparator<String> getNaturalComparator() {
		Collator collator = Collator.getInstance();
		return getNaturalComparator(collator);
	}

	/**
	 * Returns a comparator that compares contained numbers based on their
	 * numeric values and compares other parts using the given collator.
	 * 
	 * @param collator
	 *            used for locale specific comparison of text (non-number)
	 *            subwords - must not be null
	 * @return <p>
	 *         A string comparator that uses the given Collator to compare
	 *         subwords and handles embedded numbers correctly.
	 *         </p>
	 * @see #getNaturalComparator()
	 */
	public static Comparator<String> getNaturalComparator(
			final Collator collator) {
		if (collator == null) {
			// it's important to explicitly handle this here - else the bug will
			// manifest anytime later in possibly
			// unrelated code that tries to use the comparator
			throw new NullPointerException("collator must not be null");
		}
		return new Comparator<String>() {
			public int compare(String o1, String o2) {
				return compareNatural(collator, o1, o2);
			}
		};
	}

	/**
	 * Returns a comparator that compares contained numbers based on their
	 * numeric values and compares other parts based on each character's Unicode
	 * value.
	 * 
	 * @return <p>
	 *         a string comparator that does case sensitive comparisons on pure
	 *         ascii strings and handles embedded numbers correctly.
	 *         </p>
	 *         <b>Do not use</b> if your app might ever run on any locale that
	 *         uses more than 7-bit ascii characters.
	 * @see #getNaturalComparator()
	 * @see #getNaturalComparator(java.text.Collator)
	 */
	public static Comparator<String> getNaturalComparatorAscii() {
		return NATURAL_COMPARATOR_ASCII;
	}

	/**
	 * Returns a comparator that compares contained numbers based on their
	 * numeric values and compares other parts based on each character's Unicode
	 * value while ignore upper/lower case differences. <b>Do not use</b> if
	 * your app might ever run on any locale that uses more than 7-bit ascii
	 * characters.
	 * 
	 * @return <p>
	 *         a string comparator that does case insensitive comparisons on
	 *         pure ascii strings and handles embedded numbers correctly.
	 *         </p>
	 * @see #getNaturalComparator()
	 * @see #getNaturalComparator(java.text.Collator)
	 */
	public static Comparator<String> getNaturalComparatorIgnoreCaseAscii() {
		return IGNORE_CASE_NATURAL_COMPARATOR_ASCII;
	}

	/**
	 * <p>
	 * Compares two strings using the current locale's rules and comparing
	 * contained numbers based on their numeric values.
	 * </p>
	 * <p>
	 * This is probably the best default comparison to use.
	 * </p>
	 * <p>
	 * If you know that the texts to be compared are in a certain language that
	 * differs from the default locale's langage, then get a collator for the
	 * desired locale ({@link java.text.Collator#getInstance(java.util.Locale)})
	 * and pass it to
	 * {@link #compareNatural(java.text.Collator, String, String)}
	 * </p>
	 * 
	 * @param s
	 *            first string
	 * @param t
	 *            second string
	 * @return zero iff <code>s</code> and <code>t</code> are equal, a value
	 *         less than zero iff <code>s</code> lexicographically precedes
	 *         <code>t</code> and a value larger than zero iff <code>s</code>
	 *         lexicographically follows <code>t</code>
	 */
	public static int compareNatural(String s, String t) {
		return compareNatural(s, t, false, Collator.getInstance());
	}

	/**
	 * <p>
	 * Compares two strings using the given collator and comparing contained
	 * numbers based on their numeric values.
	 * </p>
	 * 
	 * @param s
	 *            first string
	 * @param t
	 *            second string
	 * @return zero iff <code>s</code> and <code>t</code> are equal, a value
	 *         less than zero iff <code>s</code> lexicographically precedes
	 *         <code>t</code> and a value larger than zero iff <code>s</code>
	 *         lexicographically follows <code>t</code>
	 */
	public static int compareNatural(Collator collator, String s, String t) {
		return compareNatural(s, t, true, collator);
	}

	/**
	 * <p>
	 * Compares two strings using each character's Unicode value for non-digit
	 * characters and the numeric values off any contained numbers.
	 * </p>
	 * <p>
	 * (This will probably make sense only for strings containing 7-bit ascii
	 * characters only.)
	 * </p>
	 * 
	 * @return zero iff <code>s</code> and <code>t</code> are equal, a value
	 *         less than zero iff <code>s</code> lexicographically precedes
	 *         <code>t</code> and a value larger than zero iff <code>s</code>
	 *         lexicographically follows <code>t</code>
	 */
	public static int compareNaturalAscii(String s, String t) {
		return compareNatural(s, t, true, null);
	}

	/**
	 * <p>
	 * Compares two strings using each character's Unicode value - ignoring
	 * upper/lower case - for non-digit characters and the numeric values of any
	 * contained numbers.
	 * </p>
	 * <p>
	 * (This will probably make sense only for strings containing 7-bit ascii
	 * characters only.)
	 * </p>
	 * 
	 * @return zero iff <code>s</code> and <code>t</code> are equal, a value
	 *         less than zero iff <code>s</code> lexicographically precedes
	 *         <code>t</code> and a value larger than zero iff <code>s</code>
	 *         lexicographically follows <code>t</code>
	 */
	public static int compareNaturalIgnoreCaseAscii(String s, String t) {
		return compareNatural(s, t, false, null);
	}

	/**
	 * @param s
	 *            first string
	 * @param t
	 *            second string
	 * @param caseSensitive
	 *            treat characters differing in case only as equal - will be
	 *            ignored if a collator is given
	 * @param collator
	 *            used to compare subwords that aren't numbers - if null,
	 *            characters will be compared individually based on their
	 *            Unicode value
	 * @return zero iff <code>s</code> and <code>t</code> are equal, a value
	 *         less than zero iff <code>s</code> lexicographically precedes
	 *         <code>t</code> and a value larger than zero iff <code>s</code>
	 *         lexicographically follows <code>t</code>
	 */
	private static int compareNatural(String s, String t,
			boolean caseSensitive, Collator collator) {
		int sIndex = 0;
		int tIndex = 0;

		int sLength = s.length();
		int tLength = t.length();

		while (true) {
			// both character indices are after a subword (or at zero)

			// Check if one string is at end
			if (sIndex == sLength && tIndex == tLength) {
				return 0;
			}
			if (sIndex == sLength) {
				return -1;
			}
			if (tIndex == tLength) {
				return 1;
			}

			// Compare sub word
			char sChar = s.charAt(sIndex);
			char tChar = t.charAt(tIndex);

			boolean sCharIsDigit = Character.isDigit(sChar);
			boolean tCharIsDigit = Character.isDigit(tChar);

			if (sCharIsDigit && tCharIsDigit) {
				// Compare numbers

				// skip leading 0s
				int sLeadingZeroCount = 0;
				while (sChar == '0') {
					++sLeadingZeroCount;
					++sIndex;
					if (sIndex == sLength) {
						break;
					}
					sChar = s.charAt(sIndex);
				}
				int tLeadingZeroCount = 0;
				while (tChar == '0') {
					++tLeadingZeroCount;
					++tIndex;
					if (tIndex == tLength) {
						break;
					}
					tChar = t.charAt(tIndex);
				}
				boolean sAllZero = sIndex == sLength
						|| !Character.isDigit(sChar);
				boolean tAllZero = tIndex == tLength
						|| !Character.isDigit(tChar);
				if (sAllZero && tAllZero) {
					continue;
				}
				if (sAllZero && !tAllZero) {
					return -1;
				}
				if (tAllZero) {
					return 1;
				}

				int diff = 0;
				do {
					if (diff == 0) {
						diff = sChar - tChar;
					}
					++sIndex;
					++tIndex;
					if (sIndex == sLength && tIndex == tLength) {
						return diff != 0 ? diff : sLeadingZeroCount
								- tLeadingZeroCount;
					}
					if (sIndex == sLength) {
						if (diff == 0) {
							return -1;
						}
						return Character.isDigit(t.charAt(tIndex)) ? -1 : diff;
					}
					if (tIndex == tLength) {
						if (diff == 0) {
							return 1;
						}
						return Character.isDigit(s.charAt(sIndex)) ? 1 : diff;
					}
					sChar = s.charAt(sIndex);
					tChar = t.charAt(tIndex);
					sCharIsDigit = Character.isDigit(sChar);
					tCharIsDigit = Character.isDigit(tChar);
					if (!sCharIsDigit && !tCharIsDigit) {
						// both number sub words have the same length
						if (diff != 0) {
							return diff;
						}
						break;
					}
					if (!sCharIsDigit) {
						return -1;
					}
					if (!tCharIsDigit) {
						return 1;
					}
				} while (true);
			} else {
				// Compare words
				if (collator != null) {
					// To use the collator the whole subwords have to be
					// compared - character-by-character comparision
					// is not possible. So find the two subwords first
					int aw = sIndex;
					int bw = tIndex;
					do {
						++sIndex;
					} while (sIndex < sLength
							&& !Character.isDigit(s.charAt(sIndex)));
					do {
						++tIndex;
					} while (tIndex < tLength
							&& !Character.isDigit(t.charAt(tIndex)));

					String as = s.substring(aw, sIndex);
					String bs = t.substring(bw, tIndex);
					int subwordResult = collator.compare(as, bs);
					if (subwordResult != 0) {
						return subwordResult;
					}
				} else {
					// No collator specified. All characters should be ascii
					// only. Compare character-by-character.
					do {
						if (sChar != tChar) {
							if (caseSensitive) {
								return sChar - tChar;
							}
							sChar = Character.toUpperCase(sChar);
							tChar = Character.toUpperCase(tChar);
							if (sChar != tChar) {
								sChar = Character.toLowerCase(sChar);
								tChar = Character.toLowerCase(tChar);
								if (sChar != tChar) {
									return sChar - tChar;
								}
							}
						}
						++sIndex;
						++tIndex;
						if (sIndex == sLength && tIndex == tLength) {
							return 0;
						}
						if (sIndex == sLength) {
							return -1;
						}
						if (tIndex == tLength) {
							return 1;
						}
						sChar = s.charAt(sIndex);
						tChar = t.charAt(tIndex);
						sCharIsDigit = Character.isDigit(sChar);
						tCharIsDigit = Character.isDigit(tChar);
					} while (!sCharIsDigit && !tCharIsDigit);
				}
			}
		}
	}

	public static String[] matches(String string, String regexp) {
		if (regexp == null)
			throw new NullPointerException("regexp is null");
		if (string == null)
			throw new NullPointerException("string is null");
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(string);
		if (!matcher.find())
			return null;
		String[] groups = new String[matcher.groupCount() + 1];
		for (int i = 0; i <= matcher.groupCount(); i++)
			groups[i] = matcher.group(i);
		return groups;
	}

}
