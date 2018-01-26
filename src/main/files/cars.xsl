<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
    <h2>Cars Information</h2>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th>Anunciante</th>
        <th>Marca</th>
        <th>Modelo</th>
		<th>Preco</th>
        <th>Versao</th>
        <th>Combustível</th>
        <th>Quilómetros</th>
        <th>Potência</th>
        <th>Cilindrada</th>
        <th>Cor</th>
        <th>Lotacao</th>
      </tr>
      <xsl:for-each select="cars/car">
      <tr>
        <td><xsl:value-of select="anunciante" /></td>
        <td><xsl:value-of select="marca" /></td>
        <td><xsl:value-of select="modelo" /></td>
		<td><xsl:value-of select="preco" /></td>
        <td><xsl:value-of select="versao" /></td>
        <td><xsl:value-of select="combustivel" /></td>
        <td><xsl:value-of select="quilometros" /></td>
        <td><xsl:value-of select="potencia" /></td>
        <td><xsl:value-of select="cilindrada" /></td>
        <td><xsl:value-of select="cor" /></td>
        <td><xsl:value-of select="lotacao" /></td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>

