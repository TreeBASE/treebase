use Test::More;
eval "use Test::Pod::Coverage";
plan skip_all => "Test::Pod::Coverage required for testing POD coverage"
  if $@;
pod_coverage_ok('CIPRES::TreeBase::DBILogin');
pod_coverage_ok('CIPRES::TreeBase::DBIUtil');
pod_coverage_ok('CIPRES::TreeBase::RecDumper');
pod_coverage_ok('CIPRES::TreeBase::TreeBaseObjects');
pod_coverage_ok('CIPRES::TreeBase::VeryBadORM');
done_testing();