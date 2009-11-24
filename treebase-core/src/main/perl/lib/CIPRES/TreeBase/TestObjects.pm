$CIPRES::TreeBase::TestObjects::VERSION=0.1;

require CIPRES::TreeBase::VeryBadORM;


package Study;
CIPRES::TreeBase::VeryBadORM->register();

package Tree;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "TREE" }
sub id_attr { "TreeId" }

package Matrix;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "matrices" }

1;
