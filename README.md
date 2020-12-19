# Grid-Search Password-Keeping (PK)

<h2> :guardsman: Problem Statement </h2>
Current password authentication scheme is prone to shoulder surfing. 
GridSearchPK is a method through which the user can can create and authentic real-time passwords using a randomly generated grid.

To demonstarte we make an android application having the following functionality - 

<h4>Android application</h4>
<ul>
<li> Database - Firebase , Sensitive information (encrypted)
  <li>Register
   <ol>
    <li> User Name, Email ID, Password , R string
    <li> Directions - Horizontal ,Vertical 
    <li> Number of Steps - Min = 1, Max =  5
  </ol>
  <li> Login
    <ol>
      <li> Username, Password 
      <li> Grid - Random, GridSize = 5x5, 6x6, 7x7
      <li> Forget password, Forget R-String -> Email - OTP -> Reset 
    </ol>
</ul>

<h4> Algorithm </h4> 
<ul>
  <li> Random Grid Generation  - Uppercase Letters + Symbols
  <li> Verification - Along with duplicates
</ul>

<h2> Novel changes made in framework </h2>
<ol>
<li> <b> Two-Step Authentication </b> - More secure
<li> <b> Duplicates in random grid </b> - Increases usability, More secure
<li> <b> Symbols + Letters </b> - More secure
<li> <b> More steps </b> - More  secure
<li> <b> Variation in grid size </b> - More secure
<ol>
