package net.myconfig.core.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public final class RangeUtils {

	public static final String SEPARATOR = "..";
	
	public static final Function<String, Integer> INTEGER = new Function<String, Integer> () {

		@Override
		public Integer apply(String input) {
			return Integer.parseInt(input, 10);
		}
		
	};
	
	public static final Function<String, BigDecimal> DECIMAL = new Function<String, BigDecimal> () {

		@Override
		public BigDecimal apply(String input) {
			return new BigDecimal(input);
		}
		
	};
	
	private RangeUtils() {
	}

	public static <T extends Comparable<T>> Range<T> parse(String value, Function<String, T> parser) {
		Validate.notNull(parser, "Parser is required");
		if (StringUtils.isBlank(value) || StringUtils.equals(SEPARATOR, value)) {
			return Ranges.all();
		} else if (!StringUtils.contains(value, SEPARATOR)) {
			T element = parser.apply(value);
			return Ranges.atMost(element);
		} else {
			String lower = StringUtils.substringBefore(value, SEPARATOR);
			String upper = StringUtils.substringAfter(value, SEPARATOR);
			if (StringUtils.isBlank(lower)) {
				// ..n
				Pair<T, BoundType> boundary = parseUpperBoundary(upper, parser);
				return Ranges.upTo(boundary.getLeft(), boundary.getRight());
			} else if (StringUtils.isBlank(upper)) {
				// n..
				Pair<T, BoundType> boundary = parseLowerBoundary(lower, parser);
				return Ranges.downTo(boundary.getLeft(), boundary.getRight());
			} else {
				// n..m
				Pair<T, BoundType> down = parseLowerBoundary(lower, parser);
				Pair<T, BoundType> up = parseUpperBoundary(upper, parser);
				return Ranges.range(down.getLeft(), down.getRight(), up.getLeft(), up.getRight());
			}
		}
	}

	private static <T> Pair<T, BoundType> parseUpperBoundary(String upper, Function<String, T> parser) {
		BoundType bound;
		String value;
		if (upper.length() > 1) {
			String boundChar = StringUtils.right(upper, 1);
			if ("(".equals(boundChar) || "[".equals(boundChar)) {
				bound = BoundType.OPEN;
				value = StringUtils.substring(upper, 0, -1);
			} else if (")".equals(boundChar) || "]".equals(boundChar)) {
				bound = BoundType.CLOSED;
				value = StringUtils.substring(upper, 0, -1);
			} else {
				bound = BoundType.CLOSED;
				value = upper;
			}
		} else {
			bound = BoundType.CLOSED;
			value = upper;
		}
		return Pair.of(parser.apply(value), bound);
	}

	private static <T> Pair<T, BoundType> parseLowerBoundary(String lower, Function<String, T> parser) {
		BoundType bound;
		String value;
		if (lower.length() > 1) {
			String boundChar = StringUtils.left(lower, 1);
			if ("(".equals(boundChar) || "[".equals(boundChar)) {
				bound = BoundType.CLOSED;
				value = StringUtils.substring(lower, 1);
			} else if (")".equals(boundChar) || "]".equals(boundChar)) {
				bound = BoundType.OPEN;
				value = StringUtils.substring(lower, 1);
			} else {
				bound = BoundType.CLOSED;
				value = lower;
			}
		} else {
			bound = BoundType.CLOSED;
			value = lower;
		}
		return Pair.of(parser.apply(value), bound);
	}

}
