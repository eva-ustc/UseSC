<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="utf-8"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

    <xsl:template match="/">
        <html>
            <head>
                <title><xsl:value-of select="view/header/title"/></title>
            </head>
            <body>
                <form>
                    <xsl:attribute name="name"><xsl:value-of select="/view/body/form/name"/></xsl:attribute>
                    <xsl:attribute name="action"><xsl:value-of select="/view/body/form/action"/></xsl:attribute>
                    <xsl:attribute name="method"><xsl:value-of select="/view/body/form/method"/></xsl:attribute>
                    <label><xsl:value-of select="/view/body/form/textView[1]/label"/>
                        <xsl:attribute name="name">
                            <xsl:value-of select="/view/body/form/textView[1]/name"/>
                        </xsl:attribute>
                    </label><label><xsl:value-of select="/view/body/form/textView[1]/value"/></label><br/>
                    <label><xsl:value-of select="/view/body/form/textView[2]/label"/>
                        <xsl:attribute name="name">
                            <xsl:value-of select="/view/body/form/textView[2]/name"/>
                        </xsl:attribute>
                    </label><label><xsl:value-of select="/view/body/form/textView[2]/value"/></label><br/>
                    <input>
                        <xsl:attribute name="type">submit</xsl:attribute>
                        <xsl:attribute name="value">
                            <xsl:value-of select="/view/body/form/buttonView/label"/>
                        </xsl:attribute>
                        <xsl:attribute name="name"><xsl:value-of select="/view/body/form/buttonView/name"/></xsl:attribute>
                    </input>
                </form>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>