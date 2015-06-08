package nl.tue.declare.utils.prom;

import nl.tue.declare.domain.model.ActivityDefinition;
import nl.tue.declare.domain.organization.User;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class OperationalSupportUtils {
//  /**
//   *
//   * @param recommendation Recommendation
//   * @param event AbstractEvent
//   * @return boolean
//   */
//  public static boolean match(Recommendation recommendation,
//                              Event event, Team team) {
//    boolean match = false;
//    if (recommendation != null && event != null) {
//      match = matchActivity(recommendation, event.getActivity()) &&
//          matchEventType(recommendation, event);
//      if (event.getActivity() == null) {
//        match = matchUser(recommendation, event.getUser());
//        /*if (!match) {
//          if (team != null) {
//            match = matchRole(team, recommendation, event.getUser());
//          }
//        }*/
//      }
//    }
//    return match;
//  }
//
// /* public static boolean match(Recommendation recommendation,
//                             Event event) {
//    boolean match = false;
//    if (recommendation != null && event != null) {
//      match = matchActivity(recommendation, event.getActivity()) &&
//          matchEventType(recommendation, event);
//      if (event.getActivity() == null) {
//        match = matchUser(recommendation, event.getUser());
//      }
//    }
//    return match;
//  }*/
//
//  private static boolean matchActivity(Recommendation recommendation,
//                                       ActivityDefinition activity) {
//    if (activity == null) {
//      return true;
//    }
//    boolean match = recommendation.getEvent().getTask() == null; // if task not indicated in recommenation, it is a match by default
//    if (!match) { // if tack specified in recommendation
//      match = recommendation.getEvent().getTask().equals(getTask(activity)); // match if task name equals activity name
//    }
//    return match;
//  }
//
//  private static boolean matchEventType(Recommendation recommendation,
//                                        Event event) {
//    if (event == null) {
//      return true;
//    }
//    boolean match = recommendation.getEvent().getTransition() == null; // if event type not indicated in recommenation, it is a match by default
//    if (!match) {
//     // match = recommendation.getEventType().equals(""); // match if task name equals activity name
//     // if (!match) { // if tack specified in recommendation
//         match = recommendation.getEvent().getTransition().equals(ProM.getType(
//            event)); // match if task name equals activity name
//     // }
//    }
//    return match;
//  }
//
//  public static boolean matchUser(Recommendation recommendation, User user) {
//    if (user == null) {
//      return true;
//    }
//    Collection<String> authorizedUsers = recommendation.getEvent().getAuthorizedResources();  
//    boolean match = authorizedUsers.isEmpty(); // if user not indicated in recommenation, it is a match by default
//    if (!match) { // if tack specified in recommendation
//      Iterator<String> iterator = authorizedUsers.iterator();
//      while (iterator.hasNext() && !match) {
//        match = iterator.next().toUpperCase().equals(getUser(user).toUpperCase()); // match if user is contained in the list of users
//      }
//    }
//    return match;
//  }

  /*public static boolean matchRole(Team team, Recommendation recommendation,
                                  User user) {
    if (user == null) {
      return true;
    }

    Collection<String> roles = recommendation.getRoles();
    boolean match = roles.isEmpty();
    if (!match) {
      Iterator<Role> userRoles = user.getRoles().iterator(); // check system roles
      boolean inRoles = false;
      while (userRoles.hasNext() && !inRoles) {
        inRoles = roles.contains(userRoles.next().getName());
      }

      Team.UserMap teamMap = team.getMap(user); // check team roles
      boolean inTeam = false;
      int i = -1;
      while (++i < teamMap.getSize() && !inTeam) {
        inTeam = roles.contains(teamMap.get(i).getName());
      }
      match = inRoles || inTeam;
    }
    return match;
  }*/

  /**
   *
   * @param user User
   * @return String
   */
  public static String getUser(User user) {
    if (user != null) {
      return user.getUserName();
    }
    return "";
  }

  public static String getTask(ActivityDefinition activity) {
    if (activity != null) {
      return activity.getName();
    }
    return "";
  }

}
