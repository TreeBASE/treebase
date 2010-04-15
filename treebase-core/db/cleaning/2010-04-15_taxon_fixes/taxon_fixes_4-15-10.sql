
being work;

-- start by creating new records in taxon
-- I'm assuming that it is safe to start taxon_id at 600001

INSERT INTO taxon (taxon_id, ubionamebankid, ncbitaxid, name) VALUES (600001, 3877253, NULL, 'Medicago sativa sativa');

-- next create new records in taxonvariant. 
-- I'm assuming that it is safe to start taxonvariant_ids at 800000

INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800000, 487876, NULL, 3530048, 'Golenkinia parvula', 'Golenkinia parvula');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800001, 487876, NULL, 1982801, 'Golenkinia parvula Woronchin', 'Golenkinia parvula');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800002, 570355, NULL, 3307644, 'Dictyosphaerium tetrachotomum', 'Dictyosphaerium tetrachotomum');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800003, 570355, NULL, 1471279, 'Dictyosphaerium tetrachotomum Printz', 'Dictyosphaerium tetrachotomum');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800004, 5878, NULL, 10129595, 'Parachlorella kessleri (Fott et Novakova) Krienitz et al.', 'Parachlorella kessleri');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800006, 570269, NULL, 5394354, 'Danthonia chilensis var. chilensis', 'Danthonia chilensis chilensis');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800007, 570269, NULL, NULL, 'Danthonia chilensis chilensis', 'Danthonia chilensis chilensis');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800008, 570268, NULL, 5394356, 'Danthonia chilensis var. aureofulva (E. Desv.) C. Baeza, 1996', 'Danthonia chilensis aureofulva');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800009, 570268, NULL, 8585092, 'Danthonia chilensis E. Desv. var. aureofulva (E. Desv.) C. Baeza', 'Danthonia chilensis aureofulva');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800010, 570268, NULL, 10905520, 'Danthonia chilensis var. aureofulva', 'Danthonia chilensis aureofulva');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800011, 570268, NULL, NULL, 'Danthonia chilensis aureofulva', 'Danthonia chilensis aureofulva');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800012, 428221, NULL, 8583992, 'Cortaderia boliviensis M. Lyle', 'Cortaderia boliviensis');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800013, 428221, NULL, 5393919, 'Cortaderia boliviensis M. Lyle, 1996', 'Cortaderia boliviensis');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800014, 452694, NULL, 10751874, 'Danthonia paschalis', 'Danthonia paschalis');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800016, 452694, NULL, 5409860, 'Rytidosperma paschale (Pilg.) C. Baeza, 1990 [1991]', 'Rytidosperma paschale');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800018, 570088, NULL, 1142512, 'Hebeloma victoriense A. A. Holland & Pegler 1983', 'Hebeloma victoriense');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800019, 570015, NULL, NULL, 'Medicago sativa subsp. x hemicycla', 'Medicago sativa hemicycla');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800020, 62238, NULL, 10139860, 'Medicago sativa subsp. x varia', 'Medicago sativa x varia');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800021, 39943, NULL, NULL, 'Hylarana plancyi', 'Hylarana plancyi');
INSERT INTO taxonvariant (taxonvariant_id, taxon_id, lexicalqualifier, namebankid, fullname, name) VALUES (800022, 24813, NULL, 10259115, 'Sierrana maculata', 'Sierrana maculata');

-- now remap taxonlabel records

UPDATE taxonlabel SET taxonvariant_id = 599278 WHERE taxonlabel_id = 280817;
UPDATE taxonlabel SET taxonvariant_id = 113112 WHERE taxonlabel_id = 287452;
UPDATE taxonlabel SET taxonvariant_id = 457054 WHERE taxonlabel_id = 84843;
UPDATE taxonlabel SET taxonvariant_id = 790039 WHERE taxonlabel_id = 277319;
UPDATE taxonlabel SET taxonvariant_id = 790138 WHERE taxonlabel_id = 279223;
UPDATE taxonlabel SET taxonvariant_id = 457054 WHERE taxonlabel_id = 279257;
UPDATE taxonlabel SET taxonvariant_id = 564067 WHERE taxonlabel_id = 280963;
UPDATE taxonlabel SET taxonvariant_id = 800011 WHERE taxonlabel_id = 281078;
UPDATE taxonlabel SET taxonvariant_id = 800007 WHERE taxonlabel_id = 281099;
UPDATE taxonlabel SET taxonvariant_id = 27564 WHERE taxonlabel_id = 283820;
UPDATE taxonlabel SET taxonvariant_id = 790655 WHERE taxonlabel_id = 287465;
UPDATE taxonlabel SET taxonvariant_id = 646502 WHERE taxonlabel_id = 287472;
UPDATE taxonlabel SET taxonvariant_id = 646502 WHERE taxonlabel_id = 287478;

-- now remap and/or edit taxonvariant records

UPDATE taxonvariant SET lexicalqualifier = 'synonym', namebankid = 10139861, fullname = 'Medicago sativa x Medicago falcata', name =  'Medicago sativa x Medicago falcata', taxon_id = 50823 WHERE taxonvariant_id = 136320;
UPDATE taxonvariant SET lexicalqualifier = 'synonym', namebankid = 10197530, fullname = 'Fusarium fujikuroi complex', name =  'Fusarium fujikuroi complex', taxon_id = 11549 WHERE taxonvariant_id = 236540;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 5985800, fullname = 'Gibberella fujikuroi complex', name =  'Gibberella fujikuroi complex', taxon_id = 11549 WHERE taxonvariant_id = 236541;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10203168, fullname = 'Ditylenchus dipsaci from Cirsium setosum', name =  'Ditylenchus dipsaci', taxon_id = 157494 WHERE taxonvariant_id = 246611;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10203169, fullname = 'Ditylenchus dipsaci from Fragaria sp.', name =  'Ditylenchus dipsaci from Fragaria sp.', taxon_id = 157494 WHERE taxonvariant_id = 246612;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10203170, fullname = 'Ditylenchus dipsaci from Medicago sativa', name =  'Ditylenchus dipsaci', taxon_id = 157494 WHERE taxonvariant_id = 246613;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10203171, fullname = 'Ditylenchus dipsaci from Trifolium pratense', name =  'Ditylenchus dipsaci', taxon_id = 157494 WHERE taxonvariant_id = 246614;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10203172, fullname = 'Ditylenchus dipsaci from Vicia faba', name =  'Ditylenchus dipsaci', taxon_id = 157494 WHERE taxonvariant_id = 246615;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = 10264573, fullname = 'Ditylenchus dipsaci from Daucus carota', name =  'Ditylenchus dipsaci', taxon_id = 157494 WHERE taxonvariant_id = 327113;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10231531, fullname = 'Alnicola cf. scolecina GLM 37718', name =  'Alnicola scolecina', taxon_id = 221176 WHERE taxonvariant_id = 457054;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 3877253, fullname = 'Medicago sativa sativa', name =  'Medicago sativa sativa', taxon_id = 600001 WHERE taxonvariant_id = 558258;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 6054839, fullname = 'Phrynosoma Douglassi', name =  'Phrynosoma', taxon_id = 34517 WHERE taxonvariant_id = 564155;
UPDATE taxonvariant SET lexicalqualifier = 'scientific name', namebankid = NULL, fullname = 'Rytidosperma paschale', name =  'Rytidosperma paschale', taxon_id = 452694 WHERE taxonvariant_id = 599276;
UPDATE taxonvariant SET lexicalqualifier = 'synonym', namebankid = 8588062, fullname = 'Rytidosperma paschale (Pilger) C.M.Baeza', name =  'Rytidosperma paschale', taxon_id = 452694 WHERE taxonvariant_id = 599277;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10259114, fullname = 'Lithobates maculata', name =  'Lithobates maculata', taxon_id = 24813 WHERE taxonvariant_id = 785042;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 2648840, fullname = 'lunaria', name =  'lunaria', taxon_id = 148165 WHERE taxonvariant_id = 785047;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10168536, fullname = 'Pelophylax plancyi', name =  'Pelophylax plancyi', taxon_id = 39943 WHERE taxonvariant_id = 785189;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10127185, fullname = 'Peudomonas aeruginosa', name =  'Peudomonas aeruginosa', taxon_id = 24053 WHERE taxonvariant_id = 785319;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 3877252, fullname = 'Medicago sativa falcata', name =  'Medicago sativa falcata', taxon_id = 50823 WHERE taxonvariant_id = 790033;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10576538, fullname = 'Commiphora angustata', name =  'Commiphora angustata', taxon_id = 570026 WHERE taxonvariant_id = 790049;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10576579, fullname = 'Commiphora glauca', name =  'Commiphora glauca', taxon_id = 570027 WHERE taxonvariant_id = 790050;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10294310, fullname = 'Mucronella sp DJM1309', name =  'Mucronella sp DJM1309', taxon_id = 314202 WHERE taxonvariant_id = 790059;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 2648378, fullname = 'Polanisia  uniglandulosa', name =  'Polanisia  uniglandulosa C349', taxon_id = 570071 WHERE taxonvariant_id = 790111;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584783, fullname = 'Cleome uncifera', name =  'Cleome uncifera', taxon_id = 570073 WHERE taxonvariant_id = 790113;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584706, fullname = 'Cleome oxalidea', name =  'Cleome oxalidea', taxon_id = 570074 WHERE taxonvariant_id = 790114;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584531, fullname = 'Cleome africana', name =  'Cleome africana', taxon_id = 570075 WHERE taxonvariant_id = 790122;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584553, fullname = 'Cleome breyeri', name =  'Cleome breyeri', taxon_id = 570076 WHERE taxonvariant_id = 790123;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584683, fullname = 'Cleome microaustralica', name =  'Cleome microaustralica', taxon_id = 570078 WHERE taxonvariant_id = 790126;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584566, fullname = 'Cleome cleomoides', name =  'Cleome cleomoides', taxon_id = 570079 WHERE taxonvariant_id = 790127;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10584670, fullname = 'Cleome luederitziana', name =  'Cleome luederitziana', taxon_id = 570081 WHERE taxonvariant_id = 790129;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 2661891, fullname = 'Oryzopsis asperifolia', name =  'Oryzopsis asperifolia', taxon_id = 570096 WHERE taxonvariant_id = 790164;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10748019, fullname = 'Amelichloa brevipes', name =  'Amelichloa brevipes', taxon_id = 570097 WHERE taxonvariant_id = 790165;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10748017, fullname = 'Amelichloa ambigua', name =  'Amelichloa ambigua', taxon_id = 570098 WHERE taxonvariant_id = 790166;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 10751855, fullname = 'Danthonia filifolia', name =  'Danthonia filifolia', taxon_id = 570265 WHERE taxonvariant_id = 790391;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 9739171, fullname = 'Danthonia chilensis var. aureofulva (E. Desv.) C. Baeza', name =  'Danthonia chilensis aureofulva', taxon_id = 570268 WHERE taxonvariant_id = 790394;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 8886481, fullname = 'Danthonia chilensis E. Desv. var. chilensis', name =  'Danthonia chilensis chilensis', taxon_id = 570269 WHERE taxonvariant_id = 790395;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = 3870064, fullname = 'Dictyosphaerium ehrenbergianum', name =  'Dictyosphaerium ehrenbergianum', taxon_id = 570349 WHERE taxonvariant_id = 790655;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 433256, fullname = 'Dictyosphaerium ehrenbergianum Naegeli, 1849', name =  'Dictyosphaerium ehrenbergianum', taxon_id = 570349 WHERE taxonvariant_id = 790656;
UPDATE taxonvariant SET lexicalqualifier = 'canonical form', namebankid = NULL, fullname = 'Hindakia tetrachotoma', name =  'Hindakia tetrachotoma', taxon_id = 570355 WHERE taxonvariant_id = 790661;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10751874, fullname = 'Danthonia paschalis', name =  'Danthonia paschalis', taxon_id = 452694 WHERE taxonvariant_id = 800014;
UPDATE taxonvariant SET lexicalqualifier = NULL, namebankid = 10139860, fullname = 'Medicago sativa subsp. x varia', name =  'Medicago sativa x varia', taxon_id = 62238 WHERE taxonvariant_id = 800020;

-- now edit taxon records

UPDATE taxon SET ncbitaxid = 245165, ubionamebankid = 10231531, name = 'Alnicola cf. scolecina GLM 37718' WHERE taxon_id = 221176;
UPDATE taxon SET ncbitaxid = 296618, ubionamebankid = 10257505, name = 'Rytidosperma aff. pumilum CHR562184' WHERE taxon_id = 261258;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10576538, name = 'Commiphora angustata' WHERE taxon_id = 570026;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10576579, name = 'Commiphora glauca' WHERE taxon_id = 570027;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 2648378, name = 'Polanisia  uniglandulosa' WHERE taxon_id = 570071;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584783, name = 'Cleome uncifera' WHERE taxon_id = 570073;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584706, name = 'Cleome oxalidea' WHERE taxon_id = 570074;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584531, name = 'Cleome africana' WHERE taxon_id = 570075;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584553, name = 'Cleome breyeri' WHERE taxon_id = 570076;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584683, name = 'Cleome microaustralica' WHERE taxon_id = 570078;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584566, name = 'Cleome cleomoides' WHERE taxon_id = 570079;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10584670, name = 'Cleome luederitziana' WHERE taxon_id = 570081;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 2661891, name = 'Oryzopsis asperifolia' WHERE taxon_id = 570096;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10748019, name = 'Amelichloa brevipes' WHERE taxon_id = 570097;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10748017, name = 'Amelichloa ambigua' WHERE taxon_id = 570098;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = 10751855, name = 'Danthonia filifolia' WHERE taxon_id = 570265;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = NULL, name = 'Danthonia chilensis aureofulva' WHERE taxon_id = 570268;
UPDATE taxon SET ncbitaxid = NULL, ubionamebankid = NULL, name = 'Danthonia chilensis chilensis' WHERE taxon_id = 570269;
UPDATE taxon SET ncbitaxid = 745283, ubionamebankid = 3870064, name = 'Dictyosphaerium ehrenbergianum' WHERE taxon_id = 570349;
UPDATE taxon SET ncbitaxid = 745284, ubionamebankid = 3307644, name = 'Dictyosphaerium tetrachotomum' WHERE taxon_id = 570355;

-- taxonvariant records can now be deleted

DELETE FROM taxonvariant WHERE taxonvariant_id = 790047;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790055;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790136;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790140;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790373;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790390;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790393;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790584;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790633;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790634;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790635;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790658;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790663;
DELETE FROM taxonvariant WHERE taxonvariant_id = 790664;

-- finally, taxon records can be deleted

DELETE FROM taxon WHERE taxon_id = 161564;
DELETE FROM taxon WHERE taxon_id = 172733;
DELETE FROM taxon WHERE taxon_id = 172734;
DELETE FROM taxon WHERE taxon_id = 172735;
DELETE FROM taxon WHERE taxon_id = 172736;
DELETE FROM taxon WHERE taxon_id = 172737;
DELETE FROM taxon WHERE taxon_id = 271867;
DELETE FROM taxon WHERE taxon_id = 428261;
DELETE FROM taxon WHERE taxon_id = 564233;
DELETE FROM taxon WHERE taxon_id = 564366;
DELETE FROM taxon WHERE taxon_id = 564472;
DELETE FROM taxon WHERE taxon_id = 570024;
DELETE FROM taxon WHERE taxon_id = 570032;
DELETE FROM taxon WHERE taxon_id = 570036;
DELETE FROM taxon WHERE taxon_id = 570086;
DELETE FROM taxon WHERE taxon_id = 570264;
DELETE FROM taxon WHERE taxon_id = 570267;
DELETE FROM taxon WHERE taxon_id = 570329;
DELETE FROM taxon WHERE taxon_id = 570330;
DELETE FROM taxon WHERE taxon_id = 570331;
DELETE FROM taxon WHERE taxon_id = 570352;
DELETE FROM taxon WHERE taxon_id = 570357;
DELETE FROM taxon WHERE taxon_id = 570358;

commit;

