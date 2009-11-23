use ExtUtils::MakeMaker;

WriteMakefile(
    'NAME'         => 'CIPRES-TreeBase',
    'AUTHOR'       => 'Rutger Vos <rutgeraldo@gmail.com>',
    'PL_FILES'     => {},
    'EXE_FILES'    => [],
    'VERSION_FROM' => 'lib/CIPRES/TreeBase/TreeBaseObjects.pm',    
    'clean'        => {},    
    'dist'         => { 'COMPRESS' => 'gzip -9f', 'SUFFIX' => 'gz', },
);
