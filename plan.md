Egy nagy modul, ami összeköti az egyes objektumokat (mindenképpen szeretném elkerülni a singletonokat).

A modulon belül lényegében kézzel csinálnánk DI-t.

Module
  - Representation
  - Linker (Visitable -> link, link -> Visitable)
  - Writer (Contents + Path -> Output File)

Ezekből már létrehozható a
  - TooltipRenderer (mouse hover esetén megjelenít infókat)
    - A mouse hover egy Visitor, ami minden típushoz tud renderelni.

Ezekből már létrehozhatók a Section rendererek:
  - Description...
  - ReturnValue...
  Ezek csak valamilyen HTML tag-et raknak össze.
  Érdemes ezeket összefogni valamilyen osztályban.

Ezekből össze lehet kalapálni az egyes típusokat renderelő dolgokat:
  - Function...
  - EnumType...
  Ezek csak valamilyen HTML tag-et raknak össze
  Ezeket össze lehet fogni egy Visitorban (hasonlóan a TooltipRendererhez).

Szintén összerakható a Page Header és Footer renderer (minden lapon ez lényegében azonos).

Ebből már képezhetők a Page-ek valamilyen PageRenderer segítségével, ami kap megfelelő dolgokat és csak összerakja őket.

Done.
