package com.example.bookshelf.App.Domain

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.StyleSpan

class PageSplitter(
    private val pageWidth: Int,
    private val pageHeight: Int,
    private val lineSpacingMultiplier: Float,
    private val lineSpacingExtra: Int
) {
    private val pages = mutableListOf<CharSequence>()
    private var currentLine = SpannableStringBuilder()
    private var currentPage = SpannableStringBuilder()
    private var currentLineHeight = 0
    private var pageContentHeight = 0
    private var currentLineWidth = 0
    private var textLineHeight = 0

    fun append(text: String, textPaint: TextPaint) {
        textLineHeight =
            Math.ceil((textPaint.getFontMetrics(null) * lineSpacingMultiplier + lineSpacingExtra).toDouble()).toInt()
        val paragraphs = text.split("\n").dropLastWhile { it.isEmpty() }
        for (i in 0 until paragraphs.size - 1) {
            appendText(paragraphs[i], textPaint)
            appendNewLine()
        }
        if(paragraphs.isNotEmpty()){
            appendText(paragraphs.last(), textPaint)
        }
    }

    private fun appendText(text: String, textPaint: TextPaint) {
        val words = text.split(" ").dropLastWhile { it.isEmpty() }
        for (i in 0 until words.size - 1) {
            appendWord(words[i] + " ", textPaint)
        }
        if (words.isNotEmpty()){
            appendWord( words.last(), textPaint)
        }
    }

    private fun appendNewLine() {
        currentLine.append("\n")
        checkForPageEnd()
        appendLineToPage(textLineHeight)
    }

    private fun checkForPageEnd() {
        if (pageContentHeight + currentLineHeight > pageHeight) {
            pages.add(currentPage)
            currentPage = SpannableStringBuilder()
            pageContentHeight = 0
        }
    }

    private fun appendWord(appendedText: String, textPaint: TextPaint) {
        val textWidth = Math.ceil(textPaint.measureText(appendedText).toDouble()).toInt()
        if (currentLineWidth + textWidth >= pageWidth) {
            checkForPageEnd()
            appendLineToPage(textLineHeight)
        }
        appendTextToLine(appendedText, textPaint, textWidth)
    }

    private fun appendLineToPage(textLineHeight: Int) {
        currentPage.append(currentLine)
        pageContentHeight += currentLineHeight

        currentLine = SpannableStringBuilder()
        currentLineHeight = textLineHeight
        currentLineWidth = 0
    }

    private fun appendTextToLine(appendedText: String, textPaint: TextPaint, textWidth: Int) {
        currentLineHeight = Math.max(currentLineHeight, textLineHeight)
        currentLine.append(renderToSpannable(appendedText, textPaint))
        currentLineWidth += textWidth
    }

    fun getPages(): List<CharSequence> {
        val copyPages = ArrayList(pages)
        var lastPage = SpannableStringBuilder(currentPage)
        if (pageContentHeight + currentLineHeight > pageHeight) {
            copyPages.add(lastPage)
            lastPage = SpannableStringBuilder()
        }
        lastPage.append(currentLine)
        copyPages.add(lastPage)
        return copyPages
    }

    private fun renderToSpannable(text: String, textPaint: TextPaint): SpannableString {
        val spannable = SpannableString(text)

        if (textPaint.isFakeBoldText) {
            spannable.setSpan(StyleSpan(Typeface.BOLD), 0, spannable.length, 0)
        }
        return spannable
    }
}