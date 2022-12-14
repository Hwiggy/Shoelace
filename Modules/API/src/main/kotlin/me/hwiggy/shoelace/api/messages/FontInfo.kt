package me.hwiggy.shoelace.api.messages

/**
 * This enum is responsible for mapping default font information for chat formatting
 *
 * @author Hunter Wignall
 * @version August 4, 2019
 */
enum class FontInfo(private val character: Char, private val px: Int) {
    UPPERCASE_A('A', 5),
    UPPERCASE_B('B', 5),
    UPPERCASE_C('C', 5),
    UPPERCASE_D('D', 5),
    UPPERCASE_E('E', 5),
    UPPERCASE_F('F', 5),
    UPPERCASE_G('G', 5),
    UPPERCASE_H('H', 5),
    UPPERCASE_I('I', 3),
    UPPERCASE_J('J', 5),
    UPPERCASE_K('K', 5),
    UPPERCASE_L('L', 5),
    UPPERCASE_M('M', 5),
    UPPERCASE_N('N', 5),
    UPPERCASE_O('O', 5),
    UPPERCASE_P('P', 5),
    UPPERCASE_Q('Q', 5),
    UPPERCASE_R('R', 5),
    UPPERCASE_S('S', 5),
    UPPERCASE_T('T', 5),
    UPPERCASE_U('U', 5),
    UPPERCASE_V('V', 5),
    UPPERCASE_W('W', 5),
    UPPERCASE_X('X', 5),
    UPPERCASE_Y('Y', 5),
    UPPERCASE_Z('Z', 5),

    LOWERCASE_A('a', 5),
    LOWERCASE_B('b', 5),
    LOWERCASE_C('c', 5),
    LOWERCASE_D('d', 5),
    LOWERCASE_E('e', 5),
    LOWERCASE_F('f', 4),
    LOWERCASE_G('g', 5),
    LOWERCASE_H('h', 5),
    LOWERCASE_I('i', 1),
    LOWERCASE_J('j', 5),
    LOWERCASE_K('k', 4),
    LOWERCASE_L('l', 1),
    LOWERCASE_M('m', 5),
    LOWERCASE_N('n', 5),
    LOWERCASE_O('o', 5),
    LOWERCASE_P('p', 5),
    LOWERCASE_Q('q', 5),
    LOWERCASE_R('r', 5),
    LOWERCASE_S('s', 5),
    LOWERCASE_T('t', 4),
    LOWERCASE_U('u', 5),
    LOWERCASE_V('v', 5),
    LOWERCASE_W('w', 5),
    LOWERCASE_X('x', 5),
    LOWERCASE_Y('y', 5),
    LOWERCASE_Z('z', 5),

    NUM_1('1', 5),
    NUM_2('2', 5),
    NUM_3('3', 5),
    NUM_4('4', 5),
    NUM_5('5', 5),
    NUM_6('6', 5),
    NUM_7('7', 5),
    NUM_8('8', 5),
    NUM_9('9', 5),
    NUM_0('0', 5),
    EXCLAMATION_POINT('!', 1),
    AT_SYMBOL('@', 6),
    NUM_SIGN('#', 5),
    DOLLAR_SIGN('$', 5),
    PERCENT('%', 5),
    UP_ARROW('^', 5),
    AMPERSAND('&', 5),
    ASTERISK('*', 5),
    LEFT_PARENTHESIS('(', 4),
    RIGHT_PARENTHESIS(')', 4),
    MINUS('-', 5),
    UNDERSCORE('_', 5),
    PLUS_SIGN('+', 5),
    EQUALS_SIGN('=', 5),
    LEFT_CURL_BRACE('{', 4),
    RIGHT_CURL_BRACE('}', 4),
    LEFT_BRACKET('[', 3),
    RIGHT_BRACKET(']', 3),
    COLON(':', 1),
    SEMI_COLON(';', 1),
    DOUBLE_QUOTE('"', 3),
    SINGLE_QUOTE('\'', 1),
    LEFT_ARROW('<', 4),
    RIGHT_ARROW('>', 4),
    QUESTION_MARK('?', 5),
    SLASH('/', 5),
    BACK_SLASH('\\', 5),
    LINE('|', 1),
    TILDE('~', 5),
    TICK('`', 2),
    PERIOD('.', 1),
    COMMA(',', 1),
    SPACE(' ', 3),
    DEFAULT('\u0000', 4);

    fun width(bold: Boolean = false) = if (bold) px + 1 else px

    companion object {
        internal fun infoFor(c: Char): FontInfo {
            return values().firstOrNull { it.character == c } ?: DEFAULT
        }
    }
}