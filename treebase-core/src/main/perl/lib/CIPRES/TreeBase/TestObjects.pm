$CIPRES::TreeBase::TestObjects::VERSION=0.1;

require CIPRES::TreeBase::VeryBadORM;


package Study;
CIPRES::TreeBase::VeryBadORM->register();

package Tree;
CIPRES::TreeBase::VeryBadORM->register();

%subobject = (root_node => 'TreeNode');
%r_attr = (nodes => 'TreeNode');

sub table { "TREE" }
sub id_attr { "TreeId" }

package Matrix;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "matrices" }

package TreeNode;
CIPRES::TreeBase::VeryBadORM->register();

sub table { "node" }

1;
