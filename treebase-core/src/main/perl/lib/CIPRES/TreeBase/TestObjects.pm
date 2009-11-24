$CIPRES::TreeBase::TestObjects::VERSION=0.1;

require CIPRES::TreeBase::VeryBadORM;


package Matrix;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "matrices" }

package Person;
CIPRES::TreeBase::VeryBadORM->register();

package Study;
CIPRES::TreeBase::VeryBadORM->register();
%r_attr = (matrices => 'Matrix');
%r2_attr = (people => ['study_author', 'Person']);

package Tree;
CIPRES::TreeBase::VeryBadORM->register();

%subobject = (root_node => 'TreeNode');
%r_attr = (nodes => 'TreeNode');

sub table { "TREE" }
sub id_attr { "TreeId" }

package TreeNode;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "node" }

1;
