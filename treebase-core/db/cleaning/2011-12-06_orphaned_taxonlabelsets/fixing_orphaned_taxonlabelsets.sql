-- psql -U treebase_app -d treebasedev -h treebasedb-dev.nescent.org
-- 


12037

SELECT tls.taxonlabelset_id, count(tt.*)
FROM 
taxonlabelset tls JOIN 
taxonlabelset_taxonlabel tt USING (taxonlabelset_id) JOIN
taxonlabel tl USING (taxonlabel_id)
WHERE tls.study_id = 12037
GROUP BY tls.taxonlabelset_id 

23165	55
23161	100
23102	94

Set 23102 is probably orphaned

SELECT tls.*, mx.matrix_id, tb.treeblock_id
FROM 
taxonlabelset tls LEFT JOIN
matrix mx ON (tls.taxonlabelset_id = mx.taxonlabelset_id) LEFT JOIN
treeblock tb ON (tls.taxonlabelset_id = tb.taxonlabelset_id)
WHERE 
tls.taxonlabelset_id IN (23165, 23161, 23102)

SELECT DISTINCT ON (tls.study_id) tls.taxonlabelset_id, tls.study_id, ss.description
FROM 
taxonlabelset tls LEFT JOIN
matrix mx ON (tls.taxonlabelset_id = mx.taxonlabelset_id) LEFT JOIN
treeblock tb ON (tls.taxonlabelset_id = tb.taxonlabelset_id) JOIN 
study s ON (tls.study_id = s.study_id) JOIN
studystatus ss USING (studystatus_id)
WHERE 
mx.matrix_id IS NULL 
AND
tb.treeblock_id IS NULL

-- 345 studies are affected

-- 1394	22	In Progress
-- 4810	23	In Progress
-- 9450	44	In Progress
-- 11200	1019	Published
-- 13335	1919	Published
-- 14190	2264	In Progress
-- 16071	10215	In Progress
-- 16132	10317	Published
-- 16464	10335	Published
-- 16188	10339	In Progress
-- 18753	10347	In Progress
-- 16228	10356	In Progress
-- 16280	10367	In Progress
-- 16399	10399	Published
-- 16402	10400	Published
-- 16434	10413	In Progress
-- 16456	10424	In Progress
-- 16466	10425	In Progress
-- 16477	10429	Published
-- 16567	10431	In Progress
-- 16484	10433	Published
-- 16501	10434	In Progress
-- 16507	10435	Published
-- 18799	10436	Published
-- 16514	10438	Published
-- 16545	10449	In Progress
-- 16554	10452	In Progress
-- 16668	10462	In Progress
-- 16784	10464	Published
-- 16633	10468	In Progress
-- 16638	10469	Published
-- 16643	10470	In Progress
-- 16653	10474	In Progress
-- 16749	10485	Published
-- 16757	10486	In Progress
-- 16702	10489	Published
-- 16696	10492	Published
-- 16700	10493	Published
-- 16731	10504	Published
-- 16734	10507	Published
-- 16738	10508	In Progress
-- 16772	10511	Published
-- 21310	10512	Published
-- 16860	10529	In Progress
-- 16888	10530	In Progress
-- 16940	10536	Published
-- 16972	10541	Published
-- 16969	10542	Published
-- 17064	10553	Published
-- 17057	10569	In Progress
-- 17076	10570	Published
-- 17093	10583	In Progress
-- 17289	10589	In Progress
-- 17293	10593	Published
-- 17206	10603	Published
-- 17138	10604	In Progress
-- 18665	10605	In Progress
-- 17671	10608	Published
-- 17174	10613	Published
-- 17192	10623	Published
-- 17237	10635	Published
-- 17255	10639	In Progress
-- 17379	10644	Published
-- 17291	10648	Published
-- 17338	10652	Published
-- 22206	10653	Published
-- 17347	10654	Published
-- 17355	10658	Published
-- 17468	10665	Published
-- 18527	10673	In Progress
-- 17453	10679	Published
-- 17568	10680	Published
-- 17429	10684	Published
-- 17463	10689	Published
-- 17471	10692	Published
-- 17514	10698	In Progress
-- 17528	10699	In Progress
-- 17572	10703	Published
-- 17584	10711	In Progress
-- 22854	10713	Published
-- 17605	10714	Published
-- 18102	10724	Published
-- 17643	10727	Published
-- 17659	10732	Published
-- 17669	10733	In Progress
-- 17740	10736	Published
-- 17752	10740	In Progress
-- 17784	10749	Published
-- 17821	10759	In Progress
-- 17836	10766	Published
-- 17850	10772	Published
-- 17856	10774	Published
-- 17895	10786	In Progress
-- 17950	10795	In Progress
-- 17992	10797	Published
-- 17987	10803	Published
-- 17997	10812	In Progress
-- 18004	10814	In Progress
-- 20216	10817	In Progress
-- 18030	10821	Published
-- 18044	10828	In Progress
-- 18748	10830	Published
-- 18138	10831	In Progress
-- 18123	10847	Published
-- 18216	10859	In Progress
-- 18286	10861	In Progress
-- 18238	10870	In Progress
-- 18244	10876	Published
-- 18346	10888	Published
-- 18283	10898	Published
-- 18366	10902	In Progress
-- 18566	10904	In Progress
-- 21995	10905	Published
-- 18410	10908	Published
-- 18478	10927	Published
-- 18564	10938	Published
-- 18580	10940	Published
-- 18509	10942	Published
-- 18591	10950	Published
-- 18593	10951	Published
-- 18619	10961	In Progress
-- 18638	10963	Published
-- 18633	10966	Published
-- 18657	10971	In Progress
-- 18673	10977	In Progress
-- 18683	10983	Published
-- 18687	10984	Published
-- 18737	10996	Published
-- 18726	10999	In Progress
-- 18760	11009	Published
-- 18822	11020	Published
-- 22845	11021	Published
-- 18826	11022	Published
-- 18887	11032	Published
-- 18983	11056	In Progress
-- 19367	11058	Published
-- 19007	11061	Published
-- 19045	11078	In Progress
-- 19055	11079	Published
-- 19062	11080	Published
-- 19077	11083	Published
-- 19095	11088	Published
-- 19107	11089	In Progress
-- 19104	11090	Published
-- 19429	11091	In Progress
-- 19127	11093	Published
-- 19126	11097	Published
-- 19160	11111	Published
-- 19173	11112	Published
-- 19186	11114	Published
-- 19191	11115	Published
-- 19199	11116	Ready
-- 19394	11117	In Progress
-- 19205	11118	Published
-- 19213	11122	Published
-- 19260	11126	Published
-- 19274	11128	Published
-- 19318	11132	Published
-- 19337	11136	In Progress
-- 19359	11140	Published
-- 21258	11159	In Progress
-- 19424	11161	Published
-- 19454	11174	Published
-- 19462	11177	In Progress
-- 19494	11181	In Progress
-- 19497	11183	Published
-- 19503	11184	Published
-- 19513	11192	Published
-- 19523	11197	Published
-- 19540	11200	Published
-- 20145	11204	Published
-- 19682	11210	Published
-- 19576	11211	Published
-- 19601	11216	Published
-- 19662	11219	In Progress
-- 19654	11226	Published
-- 19793	11252	In Progress
-- 19828	11260	Published
-- 19829	11261	In Progress
-- 19844	11267	Published
-- 19854	11269	Published
-- 19883	11273	In Progress
-- 19893	11276	In Progress
-- 19918	11284	Published
-- 19942	11289	Published
-- 19962	11294	In Progress
-- 19979	11296	In Progress
-- 20000	11298	Published
-- 20317	11303	Published
-- 20140	11312	Published
-- 20142	11313	Published
-- 20177	11327	In Progress
-- 20207	11331	Published
-- 20190	11333	In Progress
-- 20222	11336	Published
-- 20252	11346	In Progress
-- 20258	11348	In Progress
-- 20284	11352	In Progress
-- 20283	11355	Published
-- 22141	11367	In Progress
-- 20338	11369	In Progress
-- 20802	11378	Published
-- 20375	11380	In Progress
-- 20520	11383	In Progress
-- 20424	11391	Published
-- 20419	11392	Published
-- 20468	11397	In Progress
-- 22095	11398	In Progress
-- 20503	11404	Published
-- 22263	11409	In Progress
-- 20610	11415	Published
-- 23182	11416	Published
-- 20628	11417	In Progress
-- 20631	11418	Published
-- 20670	11439	Published
-- 20719	11448	In Progress
-- 21327	11461	In Progress
-- 20964	11465	In Progress
-- 21181	11468	Published
-- 21278	11472	Published
-- 21062	11476	Published
-- 20901	11480	Published
-- 21036	11487	In Progress
-- 20941	11489	Published
-- 20962	11499	In Progress
-- 20976	11504	In Progress
-- 21067	11512	Published
-- 21052	11517	Published
-- 21141	11524	In Progress
-- 21513	11527	In Progress
-- 21106	11535	Published
-- 21112	11536	In Progress
-- 21135	11537	In Progress
-- 21188	11552	Published
-- 22436	11557	Published
-- 21216	11561	In Progress
-- 21673	11563	Published
-- 21343	11568	In Progress
-- 21243	11570	Published
-- 21246	11571	In Progress
-- 21264	11575	Published
-- 21273	11586	Published
-- 21305	11593	Published
-- 21320	11596	Published
-- 21413	11600	In Progress
-- 21349	11601	In Progress
-- 21340	11607	In Progress
-- 21345	11608	In Progress
-- 21369	11616	Published
-- 21380	11619	In Progress
-- 21390	11623	Published
-- 21387	11625	In Progress
-- 22145	11644	Published
-- 21461	11651	Published
-- 21568	11652	In Progress
-- 21520	11654	In Progress
-- 21485	11659	In Progress
-- 21750	11662	Published
-- 21611	11670	In Progress
-- 21696	11681	In Progress
-- 21712	11683	In Progress
-- 22040	11687	In Progress
-- 21762	11689	Published
-- 21774	11692	In Progress
-- 21999	11695	Published
-- 22004	11705	In Progress
-- 22019	11711	In Progress
-- 22027	11713	Published
-- 22052	11719	In Progress
-- 22089	11729	In Progress
-- 22153	11747	Published
-- 22170	11756	In Progress
-- 22183	11759	Published
-- 22225	11768	In Progress
-- 22636	11779	Published
-- 22256	11780	In Progress
-- 22293	11798	Published
-- 22348	11815	Published
-- 22362	11822	Published
-- 22390	11823	In Progress
-- 22374	11825	In Progress
-- 22387	11829	In Progress
-- 22405	11832	In Progress
-- 22450	11848	In Progress
-- 22480	11849	Published
-- 22706	11852	In Progress
-- 22494	11863	In Progress
-- 22489	11864	In Progress
-- 22519	11865	Published
-- 22496	11866	In Progress
-- 22514	11868	Published
-- 22529	11872	Published
-- 22600	11876	Published
-- 22551	11878	Published
-- 22574	11891	Published
-- 22581	11892	Published
-- 22661	11893	In Progress
-- 22608	11906	Published
-- 23365	11908	Published
-- 22632	11911	In Progress
-- 22638	11914	In Progress
-- 22652	11917	In Progress
-- 22723	11918	Published
-- 22727	11936	In Progress
-- 22883	11943	In Progress
-- 22872	11949	Published
-- 22828	11950	Published
-- 22876	11956	In Progress
-- 23160	11959	In Progress
-- 22895	11960	In Progress
-- 23072	11963	In Progress
-- 22914	11975	Published
-- 22930	11980	In Progress
-- 23021	11985	Published
-- 23399	11988	Published
-- 23539	11991	Published
-- 22979	11995	In Progress
-- 22996	12003	Published
-- 23012	12006	In Progress
-- 23061	12009	Published
-- 23080	12029	In Progress
-- 23095	12034	In Progress
-- 23146	12036	Published
-- 23102	12037	Published
-- 23203	12043	In Progress
-- 23158	12048	In Progress
-- 23174	12050	In Progress
-- 23200	12055	In Progress
-- 23211	12057	Published
-- 23226	12065	In Progress
-- 23247	12072	In Progress
-- 23274	12079	Published
-- 23290	12087	In Progress
-- 23292	12088	In Progress
-- 23342	12096	In Progress
-- 23357	12101	In Progress
-- 23401	12120	In Progress
-- 23408	12121	In Progress
-- 23428	12128	In Progress
-- 23491	12145	In Progress
-- 23509	12149	In Progress
-- 23606	12153	In Progress
-- 23532	12160	Published
-- 23590	12162	Published
-- 23608	12167	In Progress


SELECT tb.treeblock_id, stb.submission_id, pt.study_id
FROM treeblock tb LEFT JOIN 
sub_treeblock stb USING (treeblock_id) LEFT JOIN 
phylotree pt ON (tb.treeblock_id = pt.treeblock_id) 
WHERE tb.taxonlabelset_id IS NULL

-- 4781	66	
-- 4782	67	
-- 4783	68	
-- 4784	69	
-- 4785	70	
-- 4786	71	
-- 4787	72	
-- 4788	73	


